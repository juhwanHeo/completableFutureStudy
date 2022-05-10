package com.async.repository;

import com.async.domain.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class CoffeeRepositoryTest {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @BeforeEach
    public void createCoffeeTest() {
        Coffee latte = Coffee.builder().name("라떼").price(1100).build();
        Coffee mocha = Coffee.builder().name("모카").price(1300).build();
        Coffee americano = Coffee.builder().name("아메리카노").price(900).build();
        Coffee greenTee = Coffee.builder().name("녹차").price(700).build();
        Coffee iceTee = Coffee.builder().name("아이스티").price(600).build();
        Coffee strawberryLatte = Coffee.builder().name("딸기라떼").price(1500).build();
        Coffee mangoBlended = Coffee.builder().name("망고블랜디드").price(2000).build();
        Coffee hennyBlackTee = Coffee.builder().name("자몽허니블랙티").price(3000).build();
        Coffee yenLatte = Coffee.builder().name("연유라떼").price(1500).build();
        Coffee strawberryBlended = Coffee.builder().name("딸기블랜디드").price(2000).build();
        List<Coffee> list = new ArrayList<>();

        list.add(latte);
        list.add(mocha);
        list.add(americano);
        list.add(greenTee);
        list.add(iceTee);
        list.add(strawberryLatte);
        list.add(mangoBlended);
        list.add(hennyBlackTee);
        list.add(yenLatte);
        list.add(strawberryBlended);

        coffeeRepository.deleteAll();
        coffeeRepository.saveAll(list);
    }

    @Test
    @DisplayName("--커피 단일 조회--")
    public void findByNameCoffeeTest() {
        Coffee coffee = Coffee.builder().name("라떼").price(1100).build();
        coffeeRepository.findByName(coffee.getName())
                .ifPresent(result -> {
                    log.info("expected coffee id: {}, name: {}, price: {}", coffee.getId(), coffee.getName(), coffee.getPrice());
                    log.info("result coffee id: {}, name: {}, price: {}", result.getId(), result.getName(), result.getPrice());
                });
    }

    @Test
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
        result.forEach(coffee -> log.info("coffee: {}", coffee));
    }

    @Test
    @DisplayName("--커피 업데이트 (Template) 테스트--")
    public void updateTemplateCoffeeTest() {
        Coffee updateCoffee = Coffee.builder().name("latte").price(1150).build();
        coffeeRepository.updatePriceByName("latte", updateCoffee.getPrice());

        coffeeRepository.findByName("latte")
                .ifPresent(result -> {
                    log.info("expected coffee id: {}, name: {}, price: {}", updateCoffee.getId(), updateCoffee.getName(), updateCoffee.getPrice());
                    log.info("result coffee id: {}, name: {}, price: {}", result.getId(), result.getName(), result.getPrice());
                    assertEquals(updateCoffee.getPrice(), result.getPrice());
                });

    }

    @Test
    @DisplayName("--커피 전체 삭제 테스트--")
    public void deleteByNameCoffeeTest() {
        coffeeRepository.deleteByName("라떼");
    }

    @Test
    @DisplayName("--커피 전체 삭제 테스트--")
    public void deleteAllCoffeeTest() {
        coffeeRepository.deleteAll();
    }

}
