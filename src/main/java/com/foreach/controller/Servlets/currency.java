package com.foreach.controller.Servlets;

import com.foreach.Settings;
import com.foreach.controller.Validate;
import com.foreach.model.dao.CurrencyServiceMySQL;
import com.foreach.model.dto.CurrencyDTO;
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
import java.util.ArrayList;


@WebServlet(name = "Currency", value = "/currency")
public class currency extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        response.setContentType("application/json");
        JSONArray resultJson = new JSONArray();
        response.setHeader("error", "dich ne rabotaet");
        response.addHeader("error2", "kto gorel togo ne podojjesh");
        String currency = request.getParameter("currency");

        if (!Validate.checkStr(currency, 3)) {
            response.setStatus(Settings.HTTP_BAD_REQUEST);
            resultJson.put(Validate.errorTextNotValid);
        } else {
            Connection connection = Settings.getConnection();

            CurrencyServiceMySQL currencyServiceMySQL = new CurrencyServiceMySQL();
            CurrencyDTO currencyDTO = currencyServiceMySQL.getCurrencyByCode(currency);


            if (currencyDTO.id == 0) {
                response.setStatus(Settings.HTTP_NO_DATA_FOUND);
                resultJson.put(Validate.ERROR_TEXT_NO_DATA_FOUND);
            } else {
                JSONObject currentObj = new JSONObject();

                currentObj.put("id", currencyDTO.id);
                currentObj.put("name", currencyDTO.full_name);
                currentObj.put("code", currencyDTO.code);
                currentObj.put("sign", currencyDTO.sign);

                resultJson.put(currentObj);

            }
        }

        PrintWriter out = response.getWriter();
        out.println(resultJson);
        out.close();
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String code = request.getHeader("code");
        String name = request.getHeader("name");
        String sign = request.getHeader("sign");

        JSONArray resultJson = new JSONArray();

        if (!Validate.checkStr(code, 3)
         || !Validate.checkStr(name, 15)
         || !Validate.checkStr(sign, 4)
        ) {
            resultJson.put(Validate.errorTextNotValid);
        } else {
            Connection connection = Settings.getConnection();

            CurrencyServiceMySQL currencyServiceMySQL = new CurrencyServiceMySQL();

            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.code =code;
            currencyDTO.full_name = name;
            currencyDTO.sign = sign;

            CurrencyDTO currencyExists = currencyServiceMySQL.getCurrencyByCode(code);

            if (currencyExists.id != 0) {
                resultJson.put(Validate.errorTextContinue);
            } else {
                currencyServiceMySQL.insertCurrency(currencyDTO);
                resultJson.put("Запись успешно добавлена");
            }
        }

        PrintWriter out = response.getWriter();
        out.write(resultJson.toString());
        out.close();
    }
}
