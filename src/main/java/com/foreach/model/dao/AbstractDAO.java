package com.foreach.model.dao;

import com.foreach.model.dto.CurrencyDTO;

import java.util.ArrayList;

public abstract class AbstractDAO <T> {
    public abstract void insert(T currency);
    public abstract ArrayList<T> getAll();
    public abstract T getById(int id);
    public abstract void remove(T currency);
}
