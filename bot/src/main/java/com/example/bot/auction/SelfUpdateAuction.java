package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SelfUpdateAuction extends Observable {

    private Auction auction;
    private Set<Integer> bids = Collections.synchronizedSet(new LinkedHashSet<>());
    private SortedSet<Integer> validBids = new ConcurrentSkipListSet<>();
    private SortedSet<Integer> invalidBids = new ConcurrentSkipListSet<>();
    private Integer bidCredits;
    private Integer bestOccupiedCredits;
    private Integer bestAvailableOrOccupiedCredits;
    private AtomicInteger creditsUsed = new AtomicInteger(0);
    private Integer actualPosition;
    private LocalDateTime endTime;
    private List<String> stats;

    private TimerTask task;
    private Timer timer;

    public SelfUpdateAuction(Integer bidCredits, Integer bestOccupiedCredits, Integer bestAvailableOrOccupiedCredits, Auction auction) {
        this.bidCredits = bidCredits;
        this.bestOccupiedCredits = bestOccupiedCredits;
        this.bestAvailableOrOccupiedCredits = bestAvailableOrOccupiedCredits;
        this.timer = new Timer();
        this.auction = auction;
        this.task = new TimerTask() {
            @Override
            public void run() {
                updateAuction(true);
            }
        };
    }

    public void startListening() {
        updateAuction(false);
        timer.schedule(task, 2000, 2000);
    }

    private void updateAuction(boolean informChanges) {
        Set<BidResponse> bidsMade = auction.bids();
        Set<Integer> bids = bidsMade.stream().map(BidResponse::getAmount).collect(Collectors.toSet());
        TreeSet<Integer> validBids = bidsMade.stream().filter(BidResponse::isValid).map(BidResponse::getAmount).collect(Collectors.toCollection(TreeSet::new));
        TreeSet<Integer> invalidBids = bidsMade.stream().filter(b -> !b.isValid()).map(BidResponse::getAmount).collect(Collectors.toCollection(TreeSet::new));
        Integer actualPosition = bidsMade.stream().filter(BidResponse::isValid).map(BidResponse::getPosition).min(Comparator.comparing(Integer::valueOf)).orElse(10000000);
        List<String> stats = auction.stats();
        if (informChanges && (!validBids.equals(this.validBids) || !actualPosition.equals(this.actualPosition))) {
            setChanged();
            notifyObservers();
        }
        this.actualPosition = actualPosition;
        this.bids = bids;
        this.validBids = validBids;
        this.validBids = invalidBids;
        this.creditsUsed.set(this.bids.size() * this.bidCredits);
        this.endTime = auction.endTime();
        this.stats = stats;
    }

    public synchronized BidResponse bid(Integer cents) {
        BidResponse bidResponse = auction.bid(cents);
        bids.add(cents);
        if (bidResponse.isValid()) {
            validBids.add(cents);
        } else {
            invalidBids.add(cents);
        }
        creditsUsed.addAndGet(bidCredits);
        return bidResponse;
    }

    public Set<Integer> bids() {
        return this.bids;
    }

    public SortedSet<Integer> validBids() {
        return validBids;
    }

    public SortedSet<Integer> invalidBids() {
        return invalidBids;
    }

    public PremiumResponse bestOccupied() {
        PremiumResponse bestOccupied = auction.bestOccupied();
        creditsUsed.addAndGet(bestOccupiedCredits);
        return bestOccupied;
    }

    public final PremiumResponse bestAvailableOrOccupied() {
        PremiumResponse bestAvailableOrOccupied = auction.bestAvailableOrOccupied();
        creditsUsed.addAndGet(bestAvailableOrOccupiedCredits);
        return bestAvailableOrOccupied;
    }

    public final AtomicInteger creditsUsed() {
        return creditsUsed;
    }

    public final Integer position() {
        return this.actualPosition;
    }

    public final LocalDateTime endTime() {
        return this.endTime;
    }

    public final List<String> stats() {
        return this.stats;
    }
}
