package com.foreach.model.dao;

import com.foreach.Settings;
import com.foreach.model.dto.CurrencyDTO;

import java.sql.*;
import java.util.ArrayList;

public class CurrencyServiceMySQL implements CurrencyDAO{

    private  String insertCurrency = "InSeRt inTO CURRENCY (code, FULL_NAME, sign) values (?, ?, ?);";
    private String selectAllCurrencies = "select * from CURRENCY;";
    private  String selectCurrencyById = "select * from CURRENCY x where x.ID = ?;";
    private  String selectCurrencyByCode222 = "select * from CURRENCY x where x.CODE = '?';";

    @Override
    public void insertCurrency(CurrencyDTO currency)  {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(Settings.SQL_PORT, Settings.USER, Settings.PASSWORD);

            Statement statement = connection.createStatement();

            preparedStatement = connection.prepareStatement(insertCurrency);

            preparedStatement.setString(1, currency.code);
            preparedStatement.setString(2, currency.full_name);
            preparedStatement.setString(3, currency.sign);

            preparedStatement.executeUpdate();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public ArrayList<CurrencyDTO> getAllCurrencies() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ArrayList<CurrencyDTO> retArrCurrency;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(Settings.SQL_PORT, Settings.USER, Settings.PASSWORD);

            preparedStatement = connection.prepareStatement(selectAllCurrencies);
            ResultSet resultSet = preparedStatement.executeQuery();
            retArrCurrency = new ArrayList<>(resultSet.getFetchSize());

            while (resultSet.next() ) {
                CurrencyDTO currency = new CurrencyDTO();
                currency.id = resultSet.getInt("id"); ;
                currency.code = resultSet.getString("code");
                currency.sign = resultSet.getString("sign");
                currency.full_name = resultSet.getString("full_name");

                retArrCurrency.add(currency);
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return retArrCurrency;

    }


    @Override
    public CurrencyDTO getCurrencyById(int id) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        CurrencyDTO retCurrency = new CurrencyDTO();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(Settings.SQL_PORT, Settings.USER, Settings.PASSWORD);

            preparedStatement = connection.prepareStatement(selectCurrencyById);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                retCurrency.id = id ;
                retCurrency.code = resultSet.getString("code");
                retCurrency.sign = resultSet.getString("sign");
                retCurrency.full_name = resultSet.getString("full_name");
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return retCurrency;
    }
    public CurrencyDTO getCurrencyByCode(String code) {

        String selectCurrencyByCode = "select * from CURRENCY x where x.CODE = '?';";
        String selectSQL = "select * from CURRENCY x where x.CODE = '" + code + "';";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        CurrencyDTO retCurrency = new CurrencyDTO();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(Settings.SQL_PORT, Settings.USER, Settings.PASSWORD);

            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            retCurrency.id = resultSet.getInt("id"); ;
            retCurrency.code = code;
            retCurrency.sign = resultSet.getString("sign");
            retCurrency.full_name = resultSet.getString("full_name");


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {

        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return retCurrency;
    }
    @Override
    public void removeCurrency(CurrencyDTO currency) {

    }
}
