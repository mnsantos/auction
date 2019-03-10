package com.example.bot.auction.model.simulation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

@JsonSerialize()
public class BidRequest {
    private User user;
    private BigDecimal amount;
    private Integer id;

    public BidRequest(User user, BigDecimal amount, Integer id) {
        this.user = user;
        this.amount = amount;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
