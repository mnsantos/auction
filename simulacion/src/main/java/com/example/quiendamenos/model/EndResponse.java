package com.example.quiendamenos.model;

import java.util.Date;

public class EndResponse {
    private Date date;

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
