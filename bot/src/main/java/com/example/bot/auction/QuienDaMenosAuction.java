package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class QuienDaMenosAuction implements Auction {


    @Override
    public BidResponse bid(Integer cents) {
        return null;
    }

    @Override
    public List<String> stats() {
        return null;
    }

    @Override
    public Set<BidResponse> bids() {
        return null;
    }

    @Override
    public PremiumResponse bestOccupied() {
        return null;
    }

    @Override
    public PremiumResponse bestAvailableOrOccupied() {
        return null;
    }

    @Override
    public LocalDateTime endTime() {
        return null;
    }
}
