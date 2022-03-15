package com.async.service;


import com.async.repository.CoffeeRepository;
import com.async.service.impl.AsyncServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootTest
@ContextConfiguration(classes = {
        CoffeeRepository.class
        , AsyncServiceImpl.class
})
/*
* https://brunch.co.kr/@springboot/267 참고
* */
@DisplayName("-- AsyncServiceTest 테스트 -- ")
public class AsyncServiceTest {

    private static final Logger log = LogManager.getLogger(AsyncServiceTest.class);

    @Autowired
    private AsyncServiceImpl asyncService;

    @Test
    @DisplayName("-- 동기 가격 조회 테스트 -- ")
    public void getPriceTest() {
        int expectedPrice = 1100;

        int resultPrice = asyncService.getPrice("latte");

        log.info("최종 가격 전달 받음: " + resultPrice);

        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    @DisplayName("-- 비동기 가격 조회 테스트 -- ")
    public void getPriceAsyncTest() {
        int expectedPrice = 1100;

        CompletableFuture<Integer> future = asyncService.getPriceAsync("latte");
        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능 ");

        int resultPrice = future.join();
        log.info("최종 가격 전달 받음: " + resultPrice);

        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    @DisplayName("-- 비동기 가격 조회 콜백 반환 없음 테스트 -- ")
    public void getPriceAsyncNotReturnTest() {
        int expectedPrice = 1100;

        CompletableFuture<Void> future = asyncService
                .getPriceAsync("latte")
                .thenAccept(p -> {
                    log.info("콜백, 가격은 ${}원, 하지만 데이터를 반환하지 않음", p);
                    assertEquals(expectedPrice, p);
                });
        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능 , 논 블록킹");

        assertNull(future.join());
    }

    @Test
    @DisplayName("-- 비동기 가격 조회 콜백 반환 있음 테스트 -- ")
    public void getPriceAsyncReturnTest() {
        int expectedPrice = 1100 + 100;

        CompletableFuture<Void> future = asyncService
                .getPriceAsync("latte")
                .thenApply(p -> {
                    log.info("같은 쓰레드로 시작");
                    return p + 100;
                })
                .thenAccept(p -> {
                    log.info("콜백, 가격은 ${}원, 하지만 데이터를 반환하지 않음", p);
                    assertEquals(expectedPrice, p);
                });

        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능 , 논 블록킹");

        assertNull(future.join());
    }

    @Test
    @DisplayName("-- 비동기 가격 조회 콜백 다른 쓰레드 테스트 -- ")
    public void getPriceAsyncOtherThreadTest() {
        int expectedPrice = 1100 + 100;
        Executor executor = Executors.newFixedThreadPool(5);

        CompletableFuture<Void> future = asyncService
                .getPriceAsync("latte")
                .thenApplyAsync(p -> {
                    log.info("같은 쓰레드로 시작");
                    return p + 100;
                }, executor)
                .thenAcceptAsync(p -> {
                    log.info("콜백, 가격은 ${}원, 하지만 데이터를 반환하지 않음", p);
                    assertEquals(expectedPrice, p);
                }, executor);

        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능 , 논 블록킹");

        assertNull(future.join());
    }

    @Test
    @DisplayName("-- thenCombine 테스트 -- ")
    public void thenCombineTest() {
        Integer expectedPrice = 1100 + 1300;

        CompletableFuture<Integer> future1 = asyncService.getPriceAsync("latte");
        CompletableFuture<Integer> future2 = asyncService.getPriceAsync("mocha");

        Integer result = future1.thenCombine(future2, Integer::sum).join();

        assertEquals(expectedPrice, result);
    }

    @Test
    @DisplayName("-- thenCompose 테스트 -- ")
    public void thenComposeTest() {
        Integer expectedPrice = (int) (1100 * 0.9);

        CompletableFuture<Integer> future = asyncService.getPriceAsync("latte");

        Integer resultPrice = future.thenCompose(result ->
                asyncService.getDiscountPriceAsync(result)).join();

        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    @DisplayName("-- allOf 테스트 -- ")
    public void allOfTest() {
        Integer expectedPrice = 1100 + 1300 + 900;

        CompletableFuture<Integer> futureA = asyncService.getPriceAsync("latte");
        CompletableFuture<Integer> futureB = asyncService.getPriceAsync("mocha");
        CompletableFuture<Integer> futureC = asyncService.getPriceAsync("americano");

        List<CompletableFuture<Integer>> completableFutureList = Arrays.asList(futureA, futureB, futureC);

//        Integer resultPrice =  CompletableFuture.allOf(futureA, futureB, futureC)
//                        .thenApply(Void -> completableFutureList.stream()
//                                .map(CompletableFuture::join)
//                                .collect(Collectors.toList()))
//                        .join()
//                        .stream()
//                        .reduce(0, Integer::sum);

        Integer resultPrice = CompletableFuture.allOf(futureA, futureB, futureC)
                .thenApply(Void -> completableFutureList.stream()
                        .map(CompletableFuture::join)
                        .mapToInt(Integer::intValue)
                        .sum())
                .join();


        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    @DisplayName("-- anyOf 테스트 -- ")
    public void anyOfTest() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> futureB = asyncService.getPriceAsync("mocha");
        CompletableFuture<Integer> futureA = asyncService.getPriceAsync("latte");
        CompletableFuture<Integer> futureC = asyncService.getPriceAsync("americano");

        CompletableFuture<Object> future = CompletableFuture.anyOf(futureB, futureA, futureC);

        log.info("result {}", future.get());
    }
}
