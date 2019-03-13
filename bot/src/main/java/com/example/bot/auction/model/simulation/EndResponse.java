package com.example.bot.auction.model.simulation;

import java.util.Date;

public class EndResponse {
    private Date date;

    public EndResponse() {
    }

    public EndResponse(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
