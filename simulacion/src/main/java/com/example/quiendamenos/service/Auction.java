package com.example.quiendamenos.service;

import com.example.quiendamenos.model.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class Auction {

    private static final BigDecimal ONE_CENT = new BigDecimal("0.01");
    private SortedMap<BigDecimal, User> validBids;
    private SortedMap<BigDecimal, Queue<User>> invalidBids;
    private Set<BigDecimal> freeBids;
    private Map<User, Queue<BigDecimal>> userBids;
    private Date timeLimit;
    private BigDecimal maxBid;
    private int id;

    public Auction(Integer id, Date timeLimit, BigDecimal maxBid) {
        if (new Date().before(timeLimit)) {
            this.timeLimit = timeLimit;
        } else {
            throw new RuntimeException("Cannot start auction in the past");
        }
        this.validBids = new ConcurrentSkipListMap<>();
        this.invalidBids = new ConcurrentSkipListMap<>();
        this.userBids = new ConcurrentHashMap<>();
        this.id = id;
        this.maxBid = maxBid;
        this.freeBids = Collections.synchronizedSet(new LinkedHashSet<>());
        for (BigDecimal i = ONE_CENT; i.compareTo(maxBid) <= 0; i = i.add(ONE_CENT)) {
            freeBids.add(i);
        }
    }

    public BidResponse bid(BigDecimal amount, User user) {
        checkIfAuctionIsRunning();
        validateAmount(amount);
        int bidMapPosition;
        synchronized (this) {
            if (freeBids.contains(amount)) {
                freeBids.remove(amount);
                validBids.put(amount, user);
                bidMapPosition = getBidPosition(amount);
                addUserBid(user, amount);
                return new BidResponse(true, bidMapPosition);
            } else if (validBids.containsKey(amount)) {
                User otherUser = validBids.get(amount);
                Queue<User> userList = new ConcurrentLinkedQueue<>();
                userList.add(otherUser);
                userList.add(user);
                invalidBids.put(amount, userList);
                validBids.remove(amount);
                addUserBid(user, amount);
                return new BidResponse(false, null);
            } else {
                invalidBids.get(amount).add(user);
                addUserBid(user, amount);
                return new BidResponse(false, null);
            }
        }
    }

    private void addUserBid(User user, BigDecimal amount) {
        if (userBids.containsKey(user)) {
            userBids.get(user).add(amount);
        } else {
            Queue<BigDecimal> bids = new ConcurrentLinkedQueue<>();
            bids.add(amount);
            userBids.put(user, bids);
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

    private int getBidPosition(BigDecimal amount) {
        return validBids.headMap(amount.add(ONE_CENT)).size();
    }

    public BigDecimal bestOccupiedAmount() {
        checkIfAuctionIsRunning();
        if (!validBids.isEmpty()) {
            validBids.firstKey();
        }
        return null;
    }

    public BigDecimal bestEmptyAmount() {
        if (!freeBids.isEmpty()) {
            BigDecimal bestFreeAmount;
            synchronized (this) {
                bestFreeAmount = freeBids.iterator().next();
            }
            BigDecimal bestOccupiedAmount = bestOccupiedAmount();
            if (bestOccupiedAmount == null || bestFreeAmount.compareTo(bestOccupiedAmount) < 0) {
                return bestFreeAmount;
            }
        }
        return null;
    }

    public List<Position> stats() {
        return validBids.entrySet().stream().map(e -> new Position(e.getValue(), e.getKey())).collect(Collectors.toList());
    }

    public AuctionResult auctionResult() {
        AuctionResult auctionResult = new AuctionResult();
        auctionResult.setStats(this.stats());
        List<Cell> cells = new ArrayList<>();

        BigDecimal minusBidMapOrZero = validBids.isEmpty() ? BigDecimal.ZERO : validBids.lastKey();
        BigDecimal minusInvalidBidMapOrZero = invalidBids.isEmpty() ? BigDecimal.ZERO : invalidBids.lastKey();
        BigDecimal max = minusInvalidBidMapOrZero.max(minusBidMapOrZero);
        for (BigDecimal i = ONE_CENT; i.compareTo(max) <= 0; i = i.add(ONE_CENT)) {
            Cell cell = new Cell();
            cell.setAmount(i);
            if (validBids.containsKey(i)) {
                cell.setUsers(Arrays.asList(validBids.get(i)));
                cell.setUserQuantity(1);
                cell.setState(Cell.State.TAKEN);
            } else if (invalidBids.containsKey(i)) {
                Queue<User> users = invalidBids.get(i);
                cell.setUsers(new ArrayList<>(users));
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

    public Date getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Date timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isRunning() {
        return new Date().before(this.timeLimit);
    }

    public int getId() {
        return id;
    }

    public List<BidResponse> getUserBids(User user) {
        if (userBids.containsKey(user)) {
            return userBids.get(user).stream().map(b -> {
                if (validBids.containsKey(b)) {
                    return new BidResponse(true, getBidPosition(b));
                } else {
                    return new BidResponse(false, null);
                }
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
