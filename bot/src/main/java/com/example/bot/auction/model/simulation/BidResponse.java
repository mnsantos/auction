package com.example.bot.auction.model.simulation;

public class BidResponse {
    private boolean valid;
    private int position;
    private int positionDisplaced;

    public BidResponse() {
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

    public int getPositionDisplaced() {
        return positionDisplaced;
    }

    public void setPositionDisplaced(int positionDisplaced) {
        this.positionDisplaced = positionDisplaced;
    }
}
