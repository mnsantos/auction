package com.example.bot.bot;

import com.example.bot.auction.Auction;

import java.util.Observable;
import java.util.Observer;

public abstract class ReactiveBot extends BaseBot implements AuctionBot, Observer {

    public ReactiveBot(Auction auction, Integer creditsToUse, Integer minutesBeforeAuctionEndToStart) {
        super(auction, creditsToUse, minutesBeforeAuctionEndToStart);
    }

    @Override
    public void update(Observable o, Object arg) {
        positionChanged(((Auction) o).position());
    }

    protected abstract void positionChanged(Integer newPosition);
}
