package com.async.service;

import java.util.concurrent.Future;

public interface AsyncService {
    int getPrice(String name);
    Future<Integer> getPriceAsync(String name);
    Future<Integer> getDiscountPriceAsync(Integer price);

}
