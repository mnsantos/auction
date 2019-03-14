package com.example.bot.bot;

import com.example.bot.auction.SelfUpdateAuction;
import com.example.bot.auction.model.PremiumResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * @author Leandro Narosky
 */
public class SimpleBlockBot extends BaseBot {

    private final static Integer SECONDS_MARGIN_UNTIL_END = 3;

    private final static Integer MILISECONDS_BETWEEN_BID_REQUESTS = 300;

    private static Logger LOG = LoggerFactory
            .getLogger(SimpleBlockBot.class);

    public SimpleBlockBot(SelfUpdateAuction auction, Integer creditsToUse, Integer minutesBeforeAuctionEndToStart) {
        super(auction, creditsToUse, minutesBeforeAuctionEndToStart);
    }

    @Override
    public void executePlan() {

        Integer bestCent = findPlaceToBet();

        LOG.info("Starting to bet in {}", bestCent);

        int counter = 0;

        while (LocalDateTime.now().isBefore(endTime.plusSeconds(SECONDS_MARGIN_UNTIL_END))) {
            //this check probably won't work because the credits are updated after the response, and we are doing requests in parallel
            final Integer priceToBet = bestCent++;
            Runnable job = () -> offer(priceToBet);
            LOG.info("Creating Job to bet #{} with {}", counter++, priceToBet);
            new Thread(job).start();

            try {
                Thread.sleep(MILISECONDS_BETWEEN_BID_REQUESTS);
            } catch (InterruptedException e) {
                LOG.error("Failed sleep ", e);
            }
        }
    }

    private Integer findPlaceToBet() {
        return this.bestAvailableOrOccupied().getCentToBet();
    }

}
