package com.example.quiendamenos.model;

public class BidResponse {
    private boolean valid;
    private Integer position;

    public BidResponse(boolean valid, Integer position) {
        this.valid = valid;
        this.position = position;
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
