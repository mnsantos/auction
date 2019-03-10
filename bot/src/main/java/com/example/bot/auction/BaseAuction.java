package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public abstract class BaseAuction extends Observable implements Auction {

    protected String auctionId;
    private Set<Integer> bids = new HashSet<>();
    private Integer bidCredits;
    private Integer bestOccupiedCredits;
    private Integer bestAvailableOrOccupiedCredits;
    private Integer creditsUsed = 0;
    private Integer actualPosition = 0;

    public BaseAuction(Integer bidCredits, Integer bestOccupiedCredits, Integer bestAvailableOrOccupiedCredits, String auctionId) {
        this.bidCredits = bidCredits;
        this.bestOccupiedCredits = bestOccupiedCredits;
        this.bestAvailableOrOccupiedCredits = bestAvailableOrOccupiedCredits;
        this.auctionId = auctionId;
        this.bids = getBidsMade();
    }

    protected abstract Set<Integer> getBidsMade();

    @Override
    public final BidResponse bid(Integer cents) {
        BidResponse bidResponse = makeBid(cents);
        bids.add(cents);
        creditsUsed += bidCredits;
        return bidResponse;
    }

    public abstract BidResponse makeBid(Integer cents);

    @Override
    public final PremiumResponse bestOccupied() {
        PremiumResponse bestOccupied = getBestOccupied();
        creditsUsed += bestOccupiedCredits;
        return bestOccupied;
    }

    public abstract PremiumResponse getBestOccupied();

    @Override
    public final PremiumResponse bestAvailableOrOccupied() {
        PremiumResponse bestAvailableOrOccupied = getBestAvailableOrOccupied();
        creditsUsed += bestAvailableOrOccupiedCredits;
        return bestAvailableOrOccupied;
    }

    public abstract PremiumResponse getBestAvailableOrOccupied();

    @Override
    public final Set<Integer> bids() {
        return this.bids;
    }

    @Override
    public Integer creditsUsed() {
        return creditsUsed;
    }

    @Override
    public Integer position() {
        return this.actualPosition;
    }

    @Scheduled(fixedDelayString = "${auctionDelayInMillis:1000}")
    private void updatePosition() {
        this.actualPosition = getActualPosition();
        setChanged();
    }

    public abstract Integer getActualPosition();

}
