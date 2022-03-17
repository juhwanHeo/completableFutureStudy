package com.async.repository;

import com.async.domain.Coffee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CoffeeRepository extends MongoRepository<Coffee, String>, CustomizedRepository {
    Optional<Coffee> findByName(String name);
    void deleteByName(String name);

}
