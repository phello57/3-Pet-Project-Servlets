package com.foreach.controller.Servlets;

import com.foreach.Settings;
import com.foreach.controller.Validate;
import com.foreach.model.dao.CurrencyServiceMySQL;
import com.foreach.model.dao.RateServeiceMySQL;
import com.foreach.model.dto.CurrencyDTO;
import com.foreach.model.dto.RatesDTO;
import com.foreach.service.Convert;
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
        if (resultBase.id != 0) {

            baseCurrencyJson.put("id",   resultBase.id);
            baseCurrencyJson.put("name", resultBase.full_name);
            baseCurrencyJson.put("code", resultBase.code);
            baseCurrencyJson.put("sign", resultBase.sign);

            isBaseFound = true;
        }

        CurrencyDTO resultTarget = currencyServiceMySQL.getCurrencyByCode(targetCurr);
        if (resultTarget.id != 0) {

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

            Convert convert = new Convert();
            convert.getConvertedAmount(resultBase.code, resultTarget.code, amount, retJson);

            retJson.put("amount", amount);

        }
        PrintWriter out = response.getWriter();
        out.println(retJson);
        out.close();
    }
}


