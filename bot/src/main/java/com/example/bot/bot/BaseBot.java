package com.example.bot.bot;

import com.example.bot.BotApplication;
import com.example.bot.auction.SelfUpdateAuction;
import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseBot implements AuctionBot {

    private static Logger LOG = LoggerFactory
            .getLogger(BotApplication.class);
    protected SelfUpdateAuction auction;
    protected LocalDateTime timeToStart;
    protected LocalDateTime endTime;
    protected Integer creditsLimit;
    private TimerTask task;
    private Timer timer;

    public BaseBot(SelfUpdateAuction auction, Integer creditsLimit, Integer minutesBeforeAuctionEndToStart) {
        this.auction = auction;
        this.creditsLimit = creditsLimit;
        this.endTime = auction.endTime();
        this.timeToStart = endTime.minusMinutes(minutesBeforeAuctionEndToStart);
        this.timer = new Timer();
        this.task = new TimerTask() {
            @Override
            public void run() {
                executePlan();
            }
        };
    }

    @Override
    public void run() {
        timer.schedule(task, Date.from(timeToStart.atZone(ZoneId.systemDefault()).toInstant()));
    }

    public final BidResponse offer(int cents) {
        //checkCredits(auction.bidCredits());
        LOG.info(String.format("Making bid %s", cents));
        BidResponse bidResponse = auction.bid(cents);
        LOG.info(String.format("Credits used: %s", auction.creditsUsed()));
        LOG.info("Bet achieved positon {}", bidResponse.getPosition());
        return bidResponse;
    }

    protected void checkCredits(int creditsToBeUsed) {
        if (auction.creditsUsed().get() >= creditsLimit + creditsToBeUsed)
            throw new RuntimeException("All credits have been used");
    }

    public final PremiumResponse bestAvailableOrOccupied() {
        //checkCredits(auction.bestAvailableOrOccupiedCredits());
        LOG.info("Asking for best available or occupied");
        PremiumResponse premiumResponse = auction.bestAvailableOrOccupied();
        LOG.info(String.format("Credits used: %s", auction.creditsUsed()));
        LOG.info(String.format("Premium response: %s", premiumResponse.toString()));
        return premiumResponse;
    }

    public final PremiumResponse bestOccupied() {
        checkCredits(auction.bestOccupiedCredits());
        LOG.info("Asking for best occupied");
        PremiumResponse premiumResponse = auction.bestOccupied();
        LOG.info(String.format("Credits used: %s", auction.creditsUsed()));
        LOG.info(String.format("Premium response: %s", premiumResponse.toString()));
        return premiumResponse;
    }

    public final int position() {
        LOG.info("Asking for position");
        int position = auction.position();
        LOG.info(String.format("Position: %s", auction.position()));
        return position;
    }

    public boolean wasOffered(int i) {
        return auction.bids().contains(i);
    }

    public abstract void executePlan();
}
