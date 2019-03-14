package com.example.bot.auction.model.quiendamenos;

import java.util.List;

public class EndResponse {

    private List<EndTime> endTimes;

    public EndResponse() {
    }

    public List<EndTime> getEndTimes() {
        return endTimes;
    }

    public void setEndTimes(List<EndTime> endTimes) {
        this.endTimes = endTimes;
    }
}
