package com.foreach.model.dao;

import com.foreach.model.dto.CurrencyDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CurrencyDAO {
     void insertCurrency(CurrencyDTO currency) throws SQLException, ClassNotFoundException;
     ArrayList<CurrencyDTO> getAllCurrencies();
     CurrencyDTO getCurrencyById(int id);
     void removeCurrency(CurrencyDTO currency);


}
