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


@WebServlet(name = "Currencies", value = "/currencies")
public class currencies extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONArray resultJson = new JSONArray();
        CurrencyServiceMySQL currencyServiceMySQL = new CurrencyServiceMySQL();
            Connection connection = Settings.getConnection();
            ArrayList<CurrencyDTO> resultSet = currencyServiceMySQL.getAllCurrencies();
            
            if (resultSet.isEmpty()) {
                response.setStatus(Settings.HTTP_NO_DATA_FOUND);
                resultJson.put(Validate.ERROR_TEXT_NO_DATA_FOUND);
            } else {
                for (CurrencyDTO obj : resultSet) {
                    JSONObject currentObj = new JSONObject();

                    currentObj.put("id",   obj.id);
                    currentObj.put("name", obj.full_name);
                    currentObj.put("code", obj.code);
                    currentObj.put("sign", obj.sign);

                    resultJson.put(currentObj);
                }
            }

        PrintWriter out = response.getWriter();
        out.println(resultJson);
        out.close();
    }
}

