package com.example.bot.auction.model.quiendamenos;

public class BidResponse {
    private int res;
    private int pos;

    public BidResponse() {
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BidResponse that = (BidResponse) o;

        if (res != that.res) return false;
        return pos == that.pos;
    }

    @Override
    public int hashCode() {
        int result = res;
        result = 31 * result + pos;
        return result;
    }
}
