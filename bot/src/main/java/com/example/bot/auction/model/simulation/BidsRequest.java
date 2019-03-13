package com.example.bot.auction.model.simulation;

public class BidsRequest {
    private User user;
    private Integer id;

    public BidsRequest(User user, Integer id) {
        this.user = user;
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        return id;
    }

}
