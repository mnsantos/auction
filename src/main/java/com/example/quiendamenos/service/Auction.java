package com.example.quiendamenos.service;

import com.example.quiendamenos.model.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class Auction {

    private static final BigDecimal ONE_CENT = new BigDecimal("0.01");
    private SortedMap<BigDecimal, User> validBidMap;
    private SortedMap<BigDecimal, List<User>> invalidBidMap;
    private Set<BigDecimal> freeBidMap;
    private Date timeLimit;
    private BigDecimal maxBid;
    private int id;

    public Auction(Integer id, Date timeLimit, BigDecimal maxBid) {
        if (new Date().before(timeLimit)) {
            this.timeLimit = timeLimit;
        } else {
            throw new RuntimeException("Cannot start auction in the past");
        }
        this.validBidMap = new ConcurrentSkipListMap<>();
        this.invalidBidMap = new ConcurrentSkipListMap<>();
        this.id = id;
        this.maxBid = maxBid;
        this.freeBidMap = Collections.synchronizedSet(new LinkedHashSet<>());
        for (BigDecimal i = ONE_CENT; i.compareTo(maxBid) <= 0; i = i.add(ONE_CENT)) {
            freeBidMap.add(i);
        }
    }

    public BidResponse bid(BigDecimal amount, User user) {
        checkIfAuctionIsRunning();
        validateAmount(amount);
        int bidMapPosition;
        synchronized (this) {
            if (freeBidMap.contains(amount)) {
                freeBidMap.remove(amount);
                validBidMap.put(amount, user);
                bidMapPosition = getBidMapPosition(amount);
                return new BidResponse(true, bidMapPosition);
            } else if (validBidMap.containsKey(amount)) {
                User otherUser = validBidMap.get(amount);
                List<User> userList = new ArrayList<>();
                userList.add(otherUser);
                userList.add(user);
                invalidBidMap.put(amount, userList);
                bidMapPosition = getBidMapPosition(amount);
                validBidMap.remove(amount);
                return new BidResponse(false, 0, bidMapPosition);
            } else {
                invalidBidMap.get(amount).add(user);
                return new BidResponse(false, 0);
            }
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(ONE_CENT) < 0) {
            throw new RuntimeException("bid must be greater than one cent");
        }
        if (amount.scale() != 2) {
            throw new RuntimeException("Amount must have scale = 2");
        }
        if (amount.compareTo(maxBid) > 0) {
            throw new RuntimeException(String.format("Max bid is %s", maxBid));
        }
    }

    private int getBidMapPosition(BigDecimal amount) {
        return validBidMap.headMap(amount.add(ONE_CENT)).size();
    }

    public BigDecimal bestOccupiedAmount() {
        checkIfAuctionIsRunning();
        if (!validBidMap.isEmpty()) {
            validBidMap.firstKey();
        }
        return null;
    }

    public BigDecimal bestEmptyAmount() {
        if (!freeBidMap.isEmpty()) {
            BigDecimal bestFreeAmount;
            synchronized (this) {
                bestFreeAmount = freeBidMap.iterator().next();
            }
            BigDecimal bestOccupiedAmount = bestOccupiedAmount();
            if (bestOccupiedAmount == null || bestFreeAmount.compareTo(bestOccupiedAmount) < 0) {
                return bestFreeAmount;
            }
        }
        return null;
    }

    public List<Position> stats() {
        return validBidMap.entrySet().stream().map(e -> new Position(e.getValue(), e.getKey())).collect(Collectors.toList());
    }

    public AuctionResult auctionResult() {
        AuctionResult auctionResult = new AuctionResult();
        auctionResult.setStats(this.stats());
        List<Cell> cells = new ArrayList<>();

        BigDecimal minusBidMapOrZero = validBidMap.isEmpty() ? BigDecimal.ZERO : validBidMap.lastKey();
        BigDecimal minusInvalidBidMapOrZero = invalidBidMap.isEmpty() ? BigDecimal.ZERO : invalidBidMap.lastKey();
        BigDecimal max = minusInvalidBidMapOrZero.max(minusBidMapOrZero);
        for (BigDecimal i = ONE_CENT; i.compareTo(max) <= 0; i = i.add(ONE_CENT)) {
            Cell cell = new Cell();
            cell.setAmount(i);
            if (validBidMap.containsKey(i)) {
                cell.setUsers(Arrays.asList(validBidMap.get(i)));
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
