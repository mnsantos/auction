package com.example.quiendamenos.model;

import java.math.BigDecimal;

public class Position {
    private User user;
    private BigDecimal amount;

    public Position(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
