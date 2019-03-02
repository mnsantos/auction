package com.example.quiendamenos.service;

import com.example.quiendamenos.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuctionService {

    private SortedMap<Integer, Auction> auctionMap = new TreeMap<>();

    public int start(Date timeLimit) {
        Integer id = 1;
        if (!auctionMap.isEmpty()) {
            id = auctionMap.lastKey() + 1;
        }
        this.auctionMap.put(id, new Auction(timeLimit, id));
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

    public PremiumResponse bestPlaceOrOccupied(int id, Double range) {
        validateId(id);
        Double bestEmptyPlace = auctionMap.get(id).bestEmptyPlace();
        if (bestEmptyPlace != null) {
            return new PremiumResponse(null, buildRange(bestEmptyPlace, range));
        } else {
            return bestOccupied(id, range);
        }
    }

    public PremiumResponse bestOccupied(int id, Double range) {
        validateId(id);
        Double bestOccupiedPlace = auctionMap.get(id).bestOccupiedPlace();
        if (bestOccupiedPlace != null) {
            return new PremiumResponse(buildRange(bestOccupiedPlace, range), null);
        }
        return new PremiumResponse();
    }

    private List<Double> buildRange(Double number, Double range) {
        Double random = (double) new Random().nextInt((int) (range * 100)) / 100;
        if (number - random <= 0.01) {
            return Arrays.asList(0.01, range);
        }
        return Arrays.asList(number - random, number + range - random);
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
