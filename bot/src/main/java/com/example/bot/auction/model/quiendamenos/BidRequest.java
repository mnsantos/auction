package com.example.bot.auction.model.quiendamenos;

import java.math.BigDecimal;

public class BidRequest {
    private BigDecimal amount;

    public BidRequest() {
    }

    public BidRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
