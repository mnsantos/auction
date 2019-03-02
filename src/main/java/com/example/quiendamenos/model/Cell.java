package com.example.quiendamenos.model;

import java.math.BigDecimal;
import java.util.List;

public class Cell {

    private BigDecimal amount;
    private List<User> users;
    private int userQuantity;
    private State state;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getUserQuantity() {
        return userQuantity;
    }

    public void setUserQuantity(int userQuantity) {
        this.userQuantity = userQuantity;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        FREE, TAKEN, INVALID;
    }
}
