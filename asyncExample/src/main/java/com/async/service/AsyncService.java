package com.async.service;

import com.async.domain.Coffee;

import java.util.concurrent.Future;

public interface AsyncService {
    int getPrice(String name);
    Future<Integer> getPriceAsync(String name);
    Future<Integer> getDiscountPriceAsync(Integer price);

    Coffee findByName(String name);
    Coffee discount(Coffee coffee, int percent);

}
