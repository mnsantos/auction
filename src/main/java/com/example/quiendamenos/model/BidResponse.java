package com.example.quiendamenos.model;

public class BidResponse {
    private boolean valid;
    private int position;
    private int positionDisplaced;

    public BidResponse(boolean valid, int position, int positionDisplaced) {
        this.valid = valid;
        this.position = position;
        this.positionDisplaced = positionDisplaced;
    }

    public BidResponse(boolean valid, int position) {
        this.valid = valid;
        this.position = position;
        this.positionDisplaced = 0;
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
