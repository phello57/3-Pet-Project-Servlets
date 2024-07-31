package com.foreach.controller.Servlets;

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
import java.util.ArrayList;


@WebServlet(name = "Rates", value = "/rates")
public class rates extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONArray resultJson = new JSONArray();

        RateServeiceMySQL rateServiceMySQL = new RateServeiceMySQL();

        ArrayList<RatesDTO> resultRates = rateServiceMySQL.getAllRates();

        for (RatesDTO obj : resultRates) {

            JSONObject wrapper = new JSONObject();
            CurrencyServiceMySQL currencyServiceMySQL = new CurrencyServiceMySQL();
            wrapper.put("id", obj.id);

            CurrencyDTO resultBase = currencyServiceMySQL.getCurrencyById(obj.base_cur_id);

            JSONObject baseJson = new JSONObject();
            baseJson.put("id",   resultBase.id);
            baseJson.put("name", resultBase.full_name);
            baseJson.put("code", resultBase.code);
            baseJson.put("sign", resultBase.sign);

            wrapper.put("baseCurrency", baseJson);

            CurrencyDTO resultTarget = currencyServiceMySQL.getCurrencyById(obj.target_cur_id);

            JSONObject targetJson = new JSONObject();

            targetJson.put("id",   resultTarget.id);
            targetJson.put("name", resultTarget.full_name);
            targetJson.put("code", resultTarget.code);
            targetJson.put("sign", resultTarget.sign);

            wrapper.put("targetCurrency", targetJson);


            wrapper.put("rate", obj.rate);

            resultJson.put(wrapper);
        }

        PrintWriter out = response.getWriter();
        out.println(resultJson);
        out.close();
    }

}

