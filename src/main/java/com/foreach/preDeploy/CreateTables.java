package com.foreach.preDeploy;

import com.foreach.Settings;

import java.sql.*;
import java.util.List;

public class CreateTables {
    public static void insertRatesData(Connection connection, List<String> dataArray) {

        try {
            Statement statement = connection.createStatement();
            String insertDataSql = "InSeRt inTO RATES (BASE_CUR_ID, TARGET_CUR_ID, RATE) values (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);

            int rubId = getIdOnCurrency(statement, "RUB");

            for (int i = 0; i < dataArray.size(); i += 2) {

                int currentCurrency = getIdOnCurrency(statement, dataArray.get(i));
                if (currentCurrency == 0) continue;


                float rate = Float.parseFloat(dataArray.get(i + 1));
                preparedStatement.setInt(1, rubId);
                preparedStatement.setInt(2, currentCurrency);
                preparedStatement.setFloat(3, Math.scalb(rate, 6));
                preparedStatement.executeUpdate();
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getIdOnCurrency(Statement statement, String currency) {
        String queryGetId = "Select x.id from CURRENCY x where x.CODE = '"+ currency +"';";

        try {
            ResultSet resultSet = statement.executeQuery("Select x.id from CURRENCY x where x.CODE = '"+ currency +"';");
            if ( resultSet.next() ) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public static void insertCurrencyData(Connection connection, List<String> dataArray) {

        try {
            Statement statement = connection.createStatement();
            String insertDataSql = "InSeRt inTO CURRENCY (CODE, FULL_NAME) values (?, ?);";

            PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);


            for (int i = 0; i < dataArray.size(); i += 2) {
                preparedStatement.setString(2, dataArray.get(i));
                preparedStatement.setString(1, dataArray.get(i + 1));
                preparedStatement.executeUpdate();
            }
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTables(Connection connection) {
        String createTableSql1 = "create table CURRENCY (" +
                "ID int AUTO_INCREMENT primary key ," +
                "CODE varchar(255)," +
                "FULL_NAME varchar(255)," +
                "SIGN varchar(32)," +
                " UNIQUE INDEX INDEX_CODE (CODE) );";
        String createTableSql2 = "create table RATES (" +
                "ID int AUTO_INCREMENT primary key," +
                "FOREIGN KEY (BASE_CUR_ID)  REFERENCES CURRENCY (ID)," +
                "FOREIGN KEY (TARGET_CUR_ID)  REFERENCES CURRENCY (ID)," +
                "RATE decimal(6)," +
                "UNIQUE INDEX INDEX_BASE (BASE_CUR_ID, TARGET_CUR_ID) );";
        try {

            Statement statement = connection.createStatement();
            try {
                statement.execute("drop table currency");

                statement.execute("drop table rates");
            } catch (SQLException e) {}


            statement.execute(createTableSql1);

            statement.execute(createTableSql2);

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
