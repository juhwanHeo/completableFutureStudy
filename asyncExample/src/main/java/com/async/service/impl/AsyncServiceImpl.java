package com.async.service.impl;

import com.async.repository.impl.CoffeeRepositoryImpl;
import com.async.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncServiceImpl implements AsyncService {

    private CoffeeRepositoryImpl coffeeRepository;

    Executor executor = Executors.newFixedThreadPool(10);

//    public ThreadPoolTaskExecutor executor;

    @Autowired
    public AsyncServiceImpl(CoffeeRepositoryImpl coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }


    @Override
    public int getPrice(String name) {
        log.info("동기 호출 방식 가격 조회 시작");
        return coffeeRepository.getPriceByName(name);
    }

    @Override
    public CompletableFuture<Integer> getPriceAsync(String name) {
        log.info("비동기 호출 방식 가격 조회 시작");

//        CompletableFuture<Integer> future = new CompletableFuture<>();

//        new Thread(() -> {
//            log.info("새로운 쓰레드 작업 시작");
//            Integer price = coffeeRepository.getPriceByName(name);
//            future.complete(price);
//        }).start();

        return CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return coffeeRepository.getPriceByName(name);
        }, executor);
    }

    @Override
    public CompletableFuture<Integer> getDiscountPriceAsync(Integer price) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return (int) (price * 0.9);
        }, executor);
    }
}
