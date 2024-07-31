package com.foreach.controller.Servlets;

import com.foreach.Settings;
import com.foreach.controller.Validate;
import com.foreach.model.dao.CurrencyServiceMySQL;
import com.foreach.model.dao.RateServeiceMySQL;
import com.foreach.model.dto.CurrencyDTO;
import com.foreach.model.dto.RatesDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@WebServlet(name = "Rate", value = "/rate")
public class rate extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject retJson = new JSONObject();
        JSONObject baseCurrencyJson = new JSONObject();
        JSONObject targetCurrencyJson = new JSONObject();

        boolean isBasedFound = false;
        boolean isTargetFound = false;

        String base = request.getParameter("base");
        String target = request.getParameter("target");


        if (!Validate.checkStr(base, 3)
                || !Validate.checkStr(target, 3)
        ) {
            response.setStatus(Settings.HTTP_NO_DATA_FOUND);
            retJson.put("message", Validate.ERROR_TEXT_NO_DATA_FOUND);
        } else {
                Connection connection = Settings.getConnection();

                RateServeiceMySQL rateServiceMySQL = new RateServeiceMySQL();
                CurrencyServiceMySQL currencyServiceMySQL =  new CurrencyServiceMySQL();
                RatesDTO resultRate = rateServiceMySQL.getRateByBaseAndTargetOnCode(base, target);

                retJson.put("id", resultRate.id );
                retJson.put("rate", resultRate.rate );

                    CurrencyDTO resultBase = currencyServiceMySQL.getCurrencyByCode(base);
                    if (resultBase != null) {

                        baseCurrencyJson.put("id",   resultBase.id);
                        baseCurrencyJson.put("name", resultBase.full_name);
                        baseCurrencyJson.put("code", resultBase.code);
                        baseCurrencyJson.put("sign", resultBase.sign);

                        isBasedFound = true;
                    }

                    CurrencyDTO resultTarget = currencyServiceMySQL.getCurrencyByCode(target);
                    if (resultTarget != null) {

                        targetCurrencyJson.put("id",   resultTarget.id);
                        targetCurrencyJson.put("name", resultTarget.full_name);
                        targetCurrencyJson.put("code", resultTarget.code);
                        targetCurrencyJson.put("sign", resultTarget.sign);

                        isTargetFound = true;
                    }

                    retJson.put("targetCurrency", targetCurrencyJson);
                    retJson.put("baseCurrency", baseCurrencyJson);



                    if (!isBasedFound  || !isTargetFound) {
                        retJson.clear();
                        response.setStatus(Settings.HTTP_NO_DATA_FOUND);
                        retJson.put("message", Validate.ERROR_TEXT_NO_DATA_FOUND);
                    }
                    if (resultRate == null) {
                        retJson.clear();
                        response.setStatus(Settings.HTTP_NO_DATA_FOUND);
                        retJson.put("message", Validate.ERROR_TEXT_NO_DATA_FOUND);
                    }
        }


        PrintWriter out = response.getWriter();
        out.println(retJson);
        out.close();
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String baseCurrency = request.getHeader("base");
        String targetCurrency = request.getHeader("target");
        float rate = Float.parseFloat(request.getHeader("rate"));

        JSONArray resultJson = new JSONArray();
        RateServeiceMySQL rateServiceMySQL = new RateServeiceMySQL();
        CurrencyServiceMySQL currencyServiceMySQL =  new CurrencyServiceMySQL();


        if (!Validate.checkStr(baseCurrency, 3)
                && !Validate.checkStr(targetCurrency, 3)
        ) {
            response.setStatus(Settings.HTTP_BAD_REQUEST);
            resultJson.put(Validate.errorTextNotValid);
        } else {
            Connection connection = Settings.getConnection();

            CurrencyDTO resultBase = currencyServiceMySQL.getCurrencyByCode(baseCurrency);
            CurrencyDTO resultTarget = currencyServiceMySQL.getCurrencyByCode(targetCurrency);

            RatesDTO ratesDTO = new RatesDTO();
            ratesDTO.base_cur_id = resultBase.id;
            ratesDTO.target_cur_id = resultTarget.id;
            ratesDTO.rate = rate;

            if (rateServiceMySQL.getRateByBaseAndTargetOnCode(resultBase.code, resultTarget.code).id == 0) {

                rateServiceMySQL.insertRate(ratesDTO);
                resultJson.put(Validate.SUCCESS_DATA_ADDED);
            } else {
                resultJson.put(Validate.errorTextContinue);
            }
        }
        response.getWriter().write(resultJson.toString());
    }
}

