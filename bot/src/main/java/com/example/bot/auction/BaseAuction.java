package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public abstract class BaseAuction extends Observable implements Auction {

    protected String auctionId;
    private Set<Integer> bids = Collections.synchronizedSet(new LinkedHashSet<>());
    private SortedSet<Integer> validBids = new ConcurrentSkipListSet<>();
    private SortedSet<Integer> invalidBids = new ConcurrentSkipListSet<>();
    private Integer bidCredits;
    private Integer bestOccupiedCredits;
    private Integer bestAvailableOrOccupiedCredits;
    private Integer creditsUsed;
    private Integer actualPosition;

    private TimerTask task;
    private Timer timer;

    public BaseAuction(Integer bidCredits, Integer bestOccupiedCredits, Integer bestAvailableOrOccupiedCredits, String auctionId) {
        this.bidCredits = bidCredits;
        this.bestOccupiedCredits = bestOccupiedCredits;
        this.bestAvailableOrOccupiedCredits = bestAvailableOrOccupiedCredits;
        this.auctionId = auctionId;
        updateAuction(false);
        this.creditsUsed = this.bids.size() * this.bidCredits;
        this.timer = new Timer();
        this.task = new TimerTask() {
            @Override
            public void run() {
                updateAuction(true);
            }
        };
        timer.schedule(task, 2000, 2000);
    }

    private void updateAuction(boolean informChanges) {
        Set<BidResponse> bidsMade = getBidsMade();
        Set<Integer> bids = bidsMade.stream().map(BidResponse::getAmount).collect(Collectors.toSet());
        TreeSet<Integer> validBids = bidsMade.stream().filter(BidResponse::isValid).map(BidResponse::getAmount).collect(Collectors.toCollection(TreeSet::new));
        TreeSet<Integer> invalidBids = bidsMade.stream().filter(b -> !b.isValid()).map(BidResponse::getAmount).collect(Collectors.toCollection(TreeSet::new));
        Integer actualPosition = getActualPosition();
        if (informChanges && (!validBids.equals(this.validBids) || !actualPosition.equals(this.actualPosition))) {
            setChanged();
            notifyObservers();
        }
        this.actualPosition = actualPosition;
        this.bids = bids;
        this.validBids = validBids;
        this.validBids = invalidBids;
    }

    protected abstract Set<BidResponse> getBidsMade();

    @Override
    public final synchronized BidResponse bid(Integer cents) {
        BidResponse bidResponse = makeBid(cents);
        bids.add(cents);
        if (bidResponse.isValid()) {
            validBids.add(cents);
        } else {
            invalidBids.add(cents);
        }
        creditsUsed += bidCredits;
        return bidResponse;
    }

    public abstract BidResponse makeBid(Integer cents);

    @Override
    public final Set<Integer> bids() {
        return this.bids;
    }

    @Override
    public final SortedSet<Integer> validBids() {
        return validBids;
    }

    @Override
    public final SortedSet<Integer> invalidBids() {
        return invalidBids;
    }

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
    public final Integer creditsUsed() {
        return creditsUsed;
    }

    @Override
    public final Integer position() {
        return this.actualPosition;
    }

    public abstract Integer getActualPosition();

}
