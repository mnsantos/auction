package com.example.bot.auction.model;

public class BidResponse {
    private int amount;
    private boolean valid;
    private int position;

    public BidResponse() {
    }

    public BidResponse(int amount, boolean valid, int position) {
        this.amount = amount;
        this.valid = valid;
        this.position = position;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
