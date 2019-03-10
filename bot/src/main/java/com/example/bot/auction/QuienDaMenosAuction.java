package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class QuienDaMenosAuction extends BaseAuction {

    public QuienDaMenosAuction(Integer bidCredits, Integer bestOccupiedCredits, Integer bestAvailableOrOccupiedCredits, String auctionId) {
        super(bidCredits, bestOccupiedCredits, bestAvailableOrOccupiedCredits, auctionId);
    }

    @Override
    protected Set<Integer> getBidsMade() {
        return null;
    }

    @Override
    public BidResponse makeBid(Integer cents) {
        return null;
    }

    @Override
    public PremiumResponse getBestOccupied() {
        return null;
    }

    @Override
    public PremiumResponse getBestAvailableOrOccupied() {
        return null;
    }

    @Override
    public Integer getActualPosition() {
        return null;
    }

    @Override
    public List<String> stats() {
        return null;
    }

    @Override
    public LocalDateTime endTime() {
        return null;
    }
}
