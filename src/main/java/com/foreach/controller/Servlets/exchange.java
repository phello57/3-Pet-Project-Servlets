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
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@WebServlet(name = "Exchange", value = "/exchange")
public class exchange extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject retJson = new JSONObject();
        JSONObject baseCurrencyJson = new JSONObject();
        JSONObject targetCurrencyJson = new JSONObject();
        CurrencyServiceMySQL currencyServiceMySQL =  new CurrencyServiceMySQL();

        boolean isBaseFound = false;
        boolean istargetFound = false;

        String baseCurr = request.getParameter("base");
        String targetCurr = request.getParameter("target");

        int amount = Integer.parseInt(request.getParameter("amount"));

            Connection connection = Settings.getConnection();

            CurrencyDTO resultBase = currencyServiceMySQL.getCurrencyByCode(baseCurr);
            if (resultBase != null) {

                baseCurrencyJson.put("id",   resultBase.id);
                baseCurrencyJson.put("name", resultBase.full_name);
                baseCurrencyJson.put("code", resultBase.code);
                baseCurrencyJson.put("sign", resultBase.sign);

                isBaseFound = true;
            }

            CurrencyDTO resultTarget = currencyServiceMySQL.getCurrencyByCode(targetCurr);
            if (resultTarget != null) {

                targetCurrencyJson.put("id",   resultTarget.id);
                targetCurrencyJson.put("name", resultTarget.full_name);
                targetCurrencyJson.put("code", resultTarget.code);
                targetCurrencyJson.put("sign", resultTarget.sign);

                istargetFound = true;
            }


            if (!istargetFound || !isBaseFound) {
                response.setStatus(Settings.HTTP_NO_DATA_FOUND);
                retJson.put("message", Validate.ERROR_TEXT_NO_DATA_FOUND);
            } else {
                retJson.put("baseCurrency", baseCurrencyJson);
                retJson.put("targetCurrency", targetCurrencyJson);

                getConvertedAmount(resultBase.code, resultTarget.code, amount, retJson);

                retJson.put("amount", amount);

            }
        PrintWriter out = response.getWriter();
        out.println(retJson);
    }
    private void getConvertedAmount(String base, String target, int amount, JSONObject json ) {
        float convertedAmount = 0;
        RateServeiceMySQL rateServiceMySQL = new RateServeiceMySQL();

        RatesDTO resultRate = rateServiceMySQL.getRateByBaseAndTargetOnCode(base, target);


        if (resultRate.id != 0 ) {
            convertedAmount = amount * resultRate.rate;
            json.put("rate", resultRate.rate);
            json.put("convertedAmount", String.format("%.2f",convertedAmount));
        } else {
            RatesDTO resultRate2 = rateServiceMySQL.getRateByBaseAndTargetOnCode(target, base);
            if ( resultRate2 != null) {
                convertedAmount = amount / resultRate2.rate;
                json.put("rate", 1 / resultRate2.rate);
                json.put("convertedAmount", String.format("%.2f",convertedAmount));
            }
        }
    }
}


