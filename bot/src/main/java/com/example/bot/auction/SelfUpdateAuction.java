package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public abstract class SelfUpdateAuction extends Observable implements Auction {

    protected String auctionId;
    private Set<Integer> bids = Collections.synchronizedSet(new LinkedHashSet<>());
    private SortedSet<Integer> validBids = new ConcurrentSkipListSet<>();
    private SortedSet<Integer> invalidBids = new ConcurrentSkipListSet<>();
    private Integer bidCredits;
    private Integer bestOccupiedCredits;
    private Integer bestAvailableOrOccupiedCredits;
    private Integer creditsUsed;
    private Integer actualPosition;
    private LocalDateTime endTime;
    private List<String> stats;

    private TimerTask task;
    private Timer timer;

    public SelfUpdateAuction(Integer bidCredits, Integer bestOccupiedCredits, Integer bestAvailableOrOccupiedCredits, String auctionId) {
        this.bidCredits = bidCredits;
        this.bestOccupiedCredits = bestOccupiedCredits;
        this.bestAvailableOrOccupiedCredits = bestAvailableOrOccupiedCredits;
        this.auctionId = auctionId;
        this.timer = new Timer();
        this.task = new TimerTask() {
            @Override
            public void run() {
                updateAuction(true);
            }
        };
    }

    @Override
    public void startListening() {
        updateAuction(false);
        timer.schedule(task, 2000, 2000);
    }

    private void updateAuction(boolean informChanges) {
        Set<BidResponse> bidsMade = getBidsMade();
        Set<Integer> bids = bidsMade.stream().map(BidResponse::getAmount).collect(Collectors.toSet());
        TreeSet<Integer> validBids = bidsMade.stream().filter(BidResponse::isValid).map(BidResponse::getAmount).collect(Collectors.toCollection(TreeSet::new));
        TreeSet<Integer> invalidBids = bidsMade.stream().filter(b -> !b.isValid()).map(BidResponse::getAmount).collect(Collectors.toCollection(TreeSet::new));
        Integer actualPosition = bidsMade.stream().filter(BidResponse::isValid).map(BidResponse::getPosition).min(Comparator.comparing(Integer::valueOf)).orElse(10000000);
        List<String> stats = this.getStats();
        if (informChanges && (!validBids.equals(this.validBids) || !actualPosition.equals(this.actualPosition))) {
            setChanged();
            notifyObservers();
        }
        this.actualPosition = actualPosition;
        this.bids = bids;
        this.validBids = validBids;
        this.validBids = invalidBids;
        this.creditsUsed = this.bids.size() * this.bidCredits;
        this.endTime = this.getEndTime();
        this.stats = stats;
    }

    protected abstract Set<BidResponse> getBidsMade();

    protected abstract LocalDateTime getEndTime();

    protected abstract List<String> getStats();

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

    @Override
    public final LocalDateTime endTime() {
        return this.endTime;
    }

    @Override
    public final List<String> stats() {
        return this.stats;
    }
}
