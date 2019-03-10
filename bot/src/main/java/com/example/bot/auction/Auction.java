package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface Auction {

    BidResponse bid(Integer cents);

    List<String> stats();

    Integer position();

    Set<Integer> bids();

    Integer creditsUsed();

    PremiumResponse bestOccupied();

    PremiumResponse bestAvailableOrOccupied();

    LocalDateTime endTime();

}
