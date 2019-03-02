package com.example.quiendamenos.service;

import com.example.quiendamenos.model.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Auction {

    private static final BigDecimal ONE_CENT = new BigDecimal("0.01");
    private SortedMap<BigDecimal, User> bidMap = new TreeMap<>();
    private SortedMap<BigDecimal, List<User>> invalidBidMap = new TreeMap<>();
    private Date timeLimit;
    private int id;
    private BigDecimal bestAmount = ONE_CENT;

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

    public synchronized BidResponse bid(BigDecimal amount, User user) {
        checkIfAuctionIsRunning();
        if (amount.equals(this.bestAmount)) {
            this.bestAmount = this.bestAmount.add(ONE_CENT);
        }
        if (invalidBidMap.containsKey(amount)) {
            invalidBidMap.get(amount).add(user);
            return new BidResponse(false, 0);
        }
        int bidMapPosition = 0;
        if (bidMap.containsKey(amount)) {
            User otherUser = bidMap.get(amount);
            List<User> userList = new ArrayList<>();
            userList.add(otherUser);
            userList.add(user);
            invalidBidMap.put(amount, userList);
            bidMapPosition = getBidMapPosition(amount);
            bidMap.remove(amount);
            return new BidResponse(false, 0, bidMapPosition);
        }
        bidMap.put(amount, user);
        bidMapPosition = getBidMapPosition(amount);
        return new BidResponse(true, bidMapPosition);
    }

    private int getBidMapPosition(BigDecimal amount) {
        return bidMap.headMap(amount.add(ONE_CENT)).size();
    }

    public BigDecimal bestOccupiedAmount() {
        checkIfAuctionIsRunning();
        if (bidMap.isEmpty()) {
            return null;
        }
        return bidMap.firstKey();
    }

    public BigDecimal bestEmptyAmount() {
        BigDecimal bestOccupiedAmount = bestOccupiedAmount();
        if (!bestAmount.equals(bestOccupiedAmount)) {
            return bestAmount;
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

        BigDecimal minusBidMapOrZero = bidMap.isEmpty() ? BigDecimal.ZERO : bidMap.lastKey();
        BigDecimal minusInvalidBidMapOrZero = invalidBidMap.isEmpty() ? BigDecimal.ZERO : invalidBidMap.lastKey();
        BigDecimal max = minusInvalidBidMapOrZero.max(minusBidMapOrZero);
        for (BigDecimal i = ONE_CENT; i.compareTo(max) <= 0; i = i.add(ONE_CENT)) {
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
