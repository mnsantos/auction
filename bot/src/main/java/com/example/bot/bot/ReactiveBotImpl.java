package com.example.bot.bot;

import com.example.bot.auction.Auction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class ReactiveBotImpl extends ReactiveBot {

    private static Logger LOG = LoggerFactory.getLogger(ReactiveBot.class);

    public ReactiveBotImpl(Auction auction, Integer creditsToUse, Integer minutesBeforeAuctionEndToStart) {
        super(auction, creditsToUse, minutesBeforeAuctionEndToStart);
    }

    @Override
    protected void positionChanged(Integer newPosition) {
    }

    public void executePlan() {
        while (LocalDateTime.now().isBefore(endTime)) {
            if (auction.position() < 20) {
                reinforceAndExterminate();
            } else {
                takePlace();
            }
        }
        LOG.info(String.format("Final position: %s", auction.position()));
    }

    private void takePlace() {

    }

    private void reinforceAndExterminate() {
        reinforce();
        exterminate();
    }

    private void exterminate() {

    }


    private void reinforce() {
    }

}
