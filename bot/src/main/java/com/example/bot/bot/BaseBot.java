package com.example.bot.bot;

import com.example.bot.auction.Auction;

import java.time.LocalDateTime;

public abstract class BaseBot implements AuctionBot {

    protected Auction auction;
    protected LocalDateTime timeToStart;
    protected LocalDateTime endTime;
    protected Integer creditsToUse;

    public BaseBot(Auction auction, Integer creditsToUse, Integer minutesBeforeAuctionEndToStart) {
        this.auction = auction;
        this.creditsToUse = creditsToUse;
        this.endTime = auction.endTime();
        this.timeToStart = endTime.minusMinutes(minutesBeforeAuctionEndToStart);
    }

    @Override
    public final void run() {
        try {
            //TODO: replace busy waiting
            while (true) {
                if (LocalDateTime.now().isBefore(timeToStart)) {
                    Thread.sleep(1000);
                }
                executePlan();
            }
        } catch (Exception e) {

        }
    }

    public abstract void executePlan();
}
