package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public interface Auction {

    void startListening();

    BidResponse bid(Integer cents);

    List<String> stats();

    Integer position();

    Set<Integer> bids();

    SortedSet<Integer> validBids();

    SortedSet<Integer> invalidBids();

    Integer creditsUsed();

    PremiumResponse bestOccupied();

    PremiumResponse bestAvailableOrOccupied();

    LocalDateTime endTime();

}
