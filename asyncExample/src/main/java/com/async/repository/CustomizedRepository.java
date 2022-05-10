package com.async.repository;

public interface CustomizedRepository {
    int getPriceByName(String name);
    void updatePriceByName(String name, int updatePrice);

}
