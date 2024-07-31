package com.foreach.model.dao;

import com.foreach.Settings;
import com.foreach.model.dto.RatesDTO;

import java.sql.*;
import java.util.ArrayList;

public class RateServeiceMySQL implements RateDAO{
    Connection connection = null;
    private String selectOnBaseAndTarget = "select * from RATES x " +
            "where x.BASE_CUR_ID   = (select d.id from CURRENCY d where d.code = ?) " +
            "and x.TARGET_CUR_ID = (select d.id from CURRENCY d where d.code = ?);"
    ;

    private String selectAllRates = "select * from RATES;";
    private String insert = "InSeRt inTO RATES (BASE_CUR_ID, TARGET_CUR_ID, RATE) values (?, ?, ?);";

    @Override
    public void insertRate(RatesDTO rate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = Settings.getConnection();

            Statement statement = connection.createStatement();


            preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setInt(1, rate.base_cur_id);
            preparedStatement.setInt(2, rate.target_cur_id);
            preparedStatement.setFloat(3, rate.rate);

            preparedStatement.executeUpdate();

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
    public ArrayList<RatesDTO> getAllRates() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ArrayList<RatesDTO> retArrRates;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(Settings.SQL_PORT, Settings.USER, Settings.PASSWORD);

            preparedStatement = connection.prepareStatement(selectAllRates);
            ResultSet resultSet = preparedStatement.executeQuery();
            retArrRates = new ArrayList<>();

            while (resultSet.next() ) {
                RatesDTO rate = new RatesDTO();
                rate.id = resultSet.getInt("id"); ;
                rate.base_cur_id = resultSet.getInt("base_cur_id");
                rate.target_cur_id = resultSet.getInt("target_cur_id");
                rate.rate = resultSet.getFloat("rate");

                retArrRates.add(rate);
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
        return retArrRates;
    }

    public RatesDTO getRateByBaseAndTargetOnCode(String base, String target) {

        PreparedStatement preparedStatement = null;

        RatesDTO retRate = new RatesDTO();

        try {
            connection = Settings.getConnection();

            Statement statement = connection.createStatement();
            preparedStatement = connection.prepareStatement(selectOnBaseAndTarget);

            preparedStatement.setString(1, base);
            preparedStatement.setString(2, target);

            ResultSet resultSet = preparedStatement.executeQuery();

            if ( resultSet.next() ) {
                retRate.id = resultSet.getInt("id"); ;
                retRate.base_cur_id = resultSet.getInt("base_cur_id");;
                retRate.target_cur_id = resultSet.getInt("target_cur_id");
                retRate.rate = resultSet.getFloat("rate");
            }


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
        return retRate;
    }
    @Override
    public void removeCurrency(RatesDTO currency) {

    }
}
