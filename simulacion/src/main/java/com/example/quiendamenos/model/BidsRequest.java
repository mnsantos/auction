package com.example.quiendamenos.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class BidsRequest {
    @NotNull
    private User user;
    @NotNull
    private Integer id;

    @JsonCreator
    public BidsRequest(@JsonProperty("user") User user, @JsonProperty("id") Integer id) {
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
