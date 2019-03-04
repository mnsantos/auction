package com.example.quiendamenos.service;

import com.example.quiendamenos.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AuctionService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final BigDecimal ONE_CENT = new BigDecimal("0.01");
    private SortedMap<Integer, Auction> auctionMap = new TreeMap<>();

    public int start(Date timeLimit, BigDecimal maxBid) {
        Integer id = 1;
        if (!auctionMap.isEmpty()) {
            id = auctionMap.lastKey() + 1;
        }
        this.auctionMap.put(id, new Auction(id, timeLimit, maxBid));
        return id;
    }

    public void stop(int id) {
        validateId(id);
        this.auctionMap.get(id).setTimeLimit(new Date());
    }

    public void extend(int id, Date newTimeLimit) {
        validateId(id);
        this.auctionMap.get(id).setTimeLimit(newTimeLimit);
    }

    public List<Position> stats(int id) {
        validateId(id);
        return auctionMap.get(id).stats();
    }

    public AuctionResult result(int id) {
        validateId(id);
        return auctionMap.get(id).auctionResult();
    }

    public PremiumResponse bestOrOccupied(int id, BigDecimal range) {
        validateId(id);
        BigDecimal bestEmptyAmount = auctionMap.get(id).bestEmptyAmount();
        if (bestEmptyAmount != null) {
            return new PremiumResponse(null, buildRange(bestEmptyAmount, range));
        } else {
            return bestOccupied(id, range);
        }
    }

    public PremiumResponse bestOccupied(int id, BigDecimal range) {
        validateId(id);
        BigDecimal bestOccupiedAmount = auctionMap.get(id).bestOccupiedAmount();
        if (bestOccupiedAmount != null) {
            return new PremiumResponse(buildRange(bestOccupiedAmount, range), null);
        }
        return new PremiumResponse();
    }

    private List<BigDecimal> buildRange(BigDecimal number, BigDecimal range) {

        BigDecimal random = new BigDecimal(new Random().nextInt(range.multiply(ONE_HUNDRED).intValue())).divide(ONE_HUNDRED);
        BigDecimal numberMinusRandom = number.add(random.negate());
        if (numberMinusRandom.compareTo(ONE_CENT) <= 0) {
            return Arrays.asList(ONE_CENT, range);
        }
        return Arrays.asList(numberMinusRandom, numberMinusRandom.add(range));
    }

    private void validateId(int id) {
        if (!auctionMap.containsKey(id)) {
            throw new RuntimeException(String.format("No auction with id %s was found", id));
        }
    }

    public BidResponse bid(BidRequest request) {
        validateId(request.getId());
        return auctionMap.get(request.getId()).bid(request.getAmount(), request.getUser());
    }

}
