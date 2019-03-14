package com.example.bot.auction;

public abstract class BaseAuction implements Auction {
    protected String auctionId;
    protected Integer bidCredits;
    protected Integer bestOccupiedCredits;
    protected Integer bestAvailableOrOccupiedCredits;

    public BaseAuction(String auctionId, Integer bidCredits, Integer bestOccupiedCredits, Integer bestAvailableOrOccupiedCredits) {
        this.auctionId = auctionId;
        this.bidCredits = bidCredits;
        this.bestOccupiedCredits = bestOccupiedCredits;
        this.bestAvailableOrOccupiedCredits = bestAvailableOrOccupiedCredits;
    }

    @Override
    public Integer bidCredits() {
        return this.bidCredits;
    }

    @Override
    public Integer bestOccupiedCredits() {
        return this.bestOccupiedCredits;
    }

    @Override
    public Integer bestAvailableOrOccupiedCredits() {
        return this.bestAvailableOrOccupiedCredits;
    }
}
