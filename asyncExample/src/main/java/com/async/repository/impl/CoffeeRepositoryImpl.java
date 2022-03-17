package com.async.repository.impl;

import com.async.domain.Coffee;
import com.async.repository.CoffeeRepository;
import com.async.repository.CustomizedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

//@Repository
public class CoffeeRepositoryImpl implements CustomizedRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

//    private final Map<String, Coffee> coffeeMap = new HashMap<>();
//
//    @PostConstruct
//    public void init() {
//        coffeeMap.put("latte", Coffee.builder().name("latte").price(1100).build());
//        coffeeMap.put("mocha", Coffee.builder().name("mocha").price(1300).build());
//        coffeeMap.put("americano", Coffee.builder().name("americano").price(900).build());
//    }

    @Override
    public int getPriceByName(String name) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Coffee coffee = mongoTemplate.findOne(Query.query(Criteria.where("name").is(name)), Coffee.class);
        return coffee != null ? coffee.getPrice() : -1;
    }

}
