package com.example.bot.auction.model.quiendamenos;

import java.math.BigDecimal;

/**
 * @author Leandro Narosky
 */
public class PremiumResponse {

    private String userm;
    private BigDecimal rmen;

    public String getUserm() {
        return userm;
    }

    public void setUserm(String userm) {
        this.userm = userm;
    }

    public BigDecimal getRmen() {
        return rmen;
    }

    public void setRmen(BigDecimal rmen) {
        this.rmen = rmen;
    }

    public Integer getCentToBet(){
        return rmen.multiply(BigDecimal.valueOf(100)).intValue();
    }
}
