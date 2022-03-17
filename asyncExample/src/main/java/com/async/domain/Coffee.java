package com.async.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "coffee")
public class Coffee {
    @Id
    private String id;

    private String name;
    private int price;

}
