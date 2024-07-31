package com.foreach.model.dao;

import com.foreach.model.dto.CurrencyDTO;
import com.foreach.model.dto.RatesDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface RateDAO {
    void insertRate(RatesDTO currency) throws SQLException, ClassNotFoundException;
    ArrayList<RatesDTO> getAllRates();
    RatesDTO getRateByBaseAndTargetOnCode(String base, String target);
    void removeCurrency(RatesDTO currency);
}
