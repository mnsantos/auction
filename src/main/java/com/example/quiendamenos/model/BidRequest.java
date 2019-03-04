package com.example.quiendamenos.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class BidRequest {
    private User user;
    private BigDecimal amount;
    private Integer id;

    @JsonCreator
    public BidRequest(@JsonProperty("user") User user, @JsonProperty("amount") BigDecimal amount, @JsonProperty("id") Integer id) {
        this.user = user;
        this.amount = amount.setScale(2, BigDecimal.ROUND_FLOOR);
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        return id;
    }

}
