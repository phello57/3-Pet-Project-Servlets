package com.foreach.controller.Servlets;

import com.foreach.preDeploy.CreateTables;
import com.foreach.preDeploy.ParseData;
import com.foreach.Settings;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@WebServlet(name = "Batcher", value = "/batcher")
public class batcher extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // сервлет нужен для того, что бы создать таблицы и заполнить их данными

        Connection connection = Settings.getConnection();
        CreateTables.createTables(connection);

        CreateTables.insertCurrencyData(connection, ParseData.getDataCurrency() );

        CreateTables.insertRatesData(connection, ParseData.getDataRates() );

    }
}
