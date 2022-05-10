package com.async.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@Document(collection = "coffee")
public class Coffee {
    @Id
    private String id;

    private String name;
    private int price;

}
