package com.example.bot.bot;

import com.example.bot.auction.SelfUpdateAuction;

import java.util.Observable;
import java.util.Observer;

public abstract class ReactiveBot extends BaseBot implements AuctionBot, Observer {

    public ReactiveBot(SelfUpdateAuction auction, Integer creditsToUse, Integer minutesBeforeAuctionEndToStart) {
        super(auction, creditsToUse, minutesBeforeAuctionEndToStart);
    }

    @Override
    public final void update(Observable o, Object arg) {
        auctionChanged();
    }

    protected abstract void auctionChanged();

}
