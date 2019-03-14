package com.example.bot.auction.model.quiendamenos;

import java.math.BigDecimal;

public class Bid {

    private int pos;
    private int unique;
    private BigDecimal amount;

    public Bid() {
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getUnique() {
        return unique;
    }

    public void setUnique(int unique) {
        this.unique = unique;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bid bid = (Bid) o;

        if (pos != bid.pos) return false;
        if (unique != bid.unique) return false;
        return amount != null ? amount.equals(bid.amount) : bid.amount == null;
    }

    @Override
    public int hashCode() {
        int result = pos;
        result = 31 * result + unique;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
