package com.async.repository;

import com.async.domain.Coffee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CoffeeRepositoryTest {

    private static final Logger log = LogManager.getLogger(CoffeeRepositoryTest.class);

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Test
    @DisplayName("--커피 생성 테스트--")
    public void createCoffeeTest() {
        Coffee latte = Coffee.builder().name("latte").price(1100).build();
        Coffee mocha = Coffee.builder().name("mocha").price(1300).build();
        Coffee americano = Coffee.builder().name("americano").price(900).build();
        List<Coffee> list = new ArrayList<>();

        list.add(latte);
        list.add(mocha);
        list.add(americano);

        coffeeRepository.saveAll(list);
    }

    @Test
    @DisplayName("--커피 단일 조회--")
    public void findByNameCoffeeTest() {
        Coffee coffee = Coffee.builder().name("latte").price(1100).build();
        coffeeRepository.findByName(coffee.getName())
                .ifPresent(result -> {
                    log.info("expected coffee name: {}, price: {}", coffee.getName(), coffee.getPrice());
                    log.info("result coffee name: {}, price: {}", result.getName(), result.getPrice());
                });
    }

    @Test
    @Ignore("ignore")
    @DisplayName("--커피 단일 조회 ( Thread )--")
    public void getPriceByNameCoffeeTest() {
        int expectedPrice = 1100;
        Coffee coffee = Coffee.builder().name("latte").price(1100).build();

        int result = coffeeRepository.getPriceByName(coffee.getName());

        assertEquals(expectedPrice, result);
    }

    @Test
    @DisplayName("--커피 전체 조회--")
    public void findAllCoffeeTest() {
        List<Coffee> result = coffeeRepository.findAll();
        result.forEach(log::info);
    }

    @Test
    @DisplayName("--커피 전체 삭제 테스트--")
    public void deleteByNameCoffeeTest() {
        coffeeRepository.deleteByName("latte");
    }

    @Test
    @DisplayName("--커피 전체 삭제 테스트--")
    public void deleteAllCoffeeTest() {
        coffeeRepository.deleteAll();
    }

}
