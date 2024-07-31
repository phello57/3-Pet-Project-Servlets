package com.foreach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Settings {
    public static final String SQL_PORT = "jdbc:mysql://127.0.0.1:3306/main_docum"; // 127.0.0.1:3306  localhost
    public static final String USER = "phello";
    public static final String PASSWORD = "2231";

    public static final int HTTP_BAD_REQUEST = 400;

    public static final int HTTP_NO_DATA_FOUND = 404;

    public static final int HTTP_SERVER_ERROR = 500;

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(Settings.SQL_PORT, Settings.USER, Settings.PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
