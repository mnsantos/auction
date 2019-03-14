package com.example.bot.auction.model.quiendamenos;

public class Position {
    private String userNam;

    public Position() {
    }

    public String getUserNam() {
        return userNam;
    }

    public void setUserNam(String userNam) {
        this.userNam = userNam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        return userNam != null ? userNam.equals(position.userNam) : position.userNam == null;
    }

    @Override
    public int hashCode() {
        return userNam != null ? userNam.hashCode() : 0;
    }
}
