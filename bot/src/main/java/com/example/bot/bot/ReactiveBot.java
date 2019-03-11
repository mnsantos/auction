package com.example.bot.bot;

import com.example.bot.auction.Auction;

import java.util.Observable;

public abstract class ReactiveBot extends BaseBot implements AuctionBot {

    public ReactiveBot(Auction auction, Integer creditsToUse, Integer minutesBeforeAuctionEndToStart) {
        super(auction, creditsToUse, minutesBeforeAuctionEndToStart);
    }

    @Override
    public final void update(Observable o, Object arg) {
        auctionChanged();
    }

    protected abstract void auctionChanged();

}
