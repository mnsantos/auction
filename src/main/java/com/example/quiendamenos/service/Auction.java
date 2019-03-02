package com.example.quiendamenos.service;

import com.example.quiendamenos.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class Auction {

    private SortedMap<Double, User> bidMap = new TreeMap<>();
    private SortedMap<Double, List<User>> invalidBidMap = new TreeMap<>();
    private Date timeLimit;
    private int id;

    public Auction(Date timeLimit, Integer id) {
        this.bidMap = new TreeMap<>();
        this.invalidBidMap = new TreeMap<>();
        this.id = id;
        if (new Date().before(timeLimit)) {
            this.timeLimit = timeLimit;
        } else {
            throw new RuntimeException("Cannot start auction in the past");
        }
    }

    public synchronized BidResponse bid(Double amount, User user) {
        checkIfAuctionIsRunning();
        if (invalidBidMap.containsKey(amount)) {
            invalidBidMap.get(amount).add(user);
            return new BidResponse(false, 0);
        }
        int bidMapPosition = 0;
        if (bidMap.containsKey(amount)) {
            User otherUser = bidMap.get(amount);
            invalidBidMap.put(amount, Arrays.asList(otherUser, user));
            bidMapPosition = getBidMapPosition(amount);
            bidMap.remove(amount);
            return new BidResponse(false, 0, bidMapPosition);
        }
        bidMap.put(amount, user);
        bidMapPosition = getBidMapPosition(amount);
        return new BidResponse(true, bidMapPosition);
    }

    private int getBidMapPosition(Double amount) {
        return bidMap.headMap(amount + 0.01).size();
    }

    public Double bestOccupiedPlace() {
        checkIfAuctionIsRunning();
        if (bidMap.isEmpty()) {
            return null;
        }
        return bidMap.firstKey();
    }

    public Double bestEmptyPlace() {
        checkIfAuctionIsRunning();
        if (bidMap.isEmpty()) {
            return 0.01;
        }

        Double minusBidMap = bidMap.firstKey();
        if (!invalidBidMap.isEmpty()) {
            Double minusInvalidBidMap = invalidBidMap.firstKey();

            if (minusBidMap >= minusInvalidBidMap) {
                SortedMap<Double, List<User>> subMap = invalidBidMap.headMap(minusBidMap);
                Double best = subMap.lastKey() + 0.01;
                if (!best.equals(minusBidMap)) {
                    return best;
                } else {
                    return null;
                }
            }
        }

        if (minusBidMap != 0.01) {
            return 0.01;
        } else {
            return null;
        }
    }

    public List<Position> stats() {
        return bidMap.entrySet().stream().map(e -> new Position(e.getValue(), e.getKey())).collect(Collectors.toList());
    }

    public AuctionResult auctionResult() {
        AuctionResult auctionResult = new AuctionResult();
        auctionResult.setStats(this.stats());
        List<Cell> cells = new ArrayList<>();

        Double minusBidMapOrZero = bidMap.isEmpty() ? 0.0 : bidMap.lastKey();
        Double minusInvalidBidMapOrZero = invalidBidMap.isEmpty() ? 0.0 : invalidBidMap.lastKey();
        Double max = Double.max(minusInvalidBidMapOrZero, minusBidMapOrZero);
        for (Double i = 0.01; i <= max; i += 0.01) {
            Cell cell = new Cell();
            cell.setAmount(i);
            if (bidMap.containsKey(i)) {
                cell.setUsers(Arrays.asList(bidMap.get(i)));
                cell.setUserQuantity(1);
                cell.setState(Cell.State.TAKEN);
            } else if (invalidBidMap.containsKey(i)) {
                List<User> users = invalidBidMap.get(i);
                cell.setUsers(users);
                cell.setUserQuantity(users.size());
                cell.setState(Cell.State.INVALID);
            } else {
                cell.setState(Cell.State.FREE);
            }
            cells.add(cell);
        }
        auctionResult.setCells(cells);
        return auctionResult;
    }

    public void checkIfAuctionIsRunning() {
        if (!isRunning()) {
            throw new RuntimeException("Auction finished");
        }
    }

    public boolean isRunning() {
        return new Date().before(this.timeLimit);
    }

    public int getId() {
        return id;
    }

    public void setTimeLimit(Date timeLimit) {
        this.timeLimit = timeLimit;
    }
}
