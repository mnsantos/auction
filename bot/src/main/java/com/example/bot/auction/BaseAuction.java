package com.example.bot.auction;

public abstract class BaseAuction implements Auction {
    protected String auctionId;

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }
}
