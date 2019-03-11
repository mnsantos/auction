package com.example.bot.bot;

import com.example.bot.auction.Auction;
import com.example.bot.auction.model.PremiumResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class ReactiveBotImpl extends ReactiveBot {

    private static Logger LOG = LoggerFactory.getLogger(ReactiveBotImpl.class);

    public ReactiveBotImpl(Auction auction, Integer creditsToUse, Integer minutesBeforeAuctionEndToStart) {
        super(auction, creditsToUse, minutesBeforeAuctionEndToStart);
    }

    @Override
    protected void auctionChanged() {

    }

    public void executePlan() {
        while (LocalDateTime.now().isBefore(endTime)) {
            if (this.position() < 20) {
                reinforce();
                exterminate();
            } else {
                takePlace();
                reinforce();
            }
        }
    }

    private void takePlace() {
        PremiumResponse premiumResponse = this.bestAvailableOrOccupied();
        Integer bestCent = premiumResponse.getBestRange() != null ? premiumResponse.getBestRange().get(0) : premiumResponse.getBestOccupiedRange().get(0);
        findValidOffer(bestCent);
    }

    private int findValidOffer(int bestCent) {
        int limit = 100;
        int jump = 1;
        int start = bestCent;
        while (true) {
            for (int i = start; i < limit + start; i += jump) {
                if (!wasOffered(i) && offer(i).isValid()) {
                    return i;
                }
            }
            limit += 100;
            jump += 1;
            start = limit + start;
        }
    }

    private void exterminate() {
        for (int i = this.auction.validBids().last(); this.auction.position() != 1 || this.auction.validBids().first().equals(i); i--) {
            if (!wasOffered(i)) {
                offer(i);
            }
        }
    }

    private void reinforce() {
        if (this.auction.validBids().size() < 50) {
            int lastCent;
            if (!this.auction.validBids().isEmpty()) {
                lastCent = this.auction.validBids().last();
            } else {
                lastCent = this.auction.invalidBids().last();
            }
            for (int i = lastCent; this.auction.validBids().size() < 50; i++) {
                if (!wasOffered(i)) {
                    offer(i);
                }
            }
        }
    }

}
