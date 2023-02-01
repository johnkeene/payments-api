package com.charter.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    String id;
    Date date;
    List<Product> products;
    Customer customer;

    int totalPrice;
    int totalPoints;

}
