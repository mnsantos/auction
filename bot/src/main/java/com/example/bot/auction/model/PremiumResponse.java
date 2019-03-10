package com.example.bot.auction.model;

import java.util.List;

public class PremiumResponse {
    private List<Integer> bestOccupiedRange;
    private List<Integer> bestRange;

    public PremiumResponse() {
    }

    public PremiumResponse(List<Integer> bestOccupiedRange, List<Integer> bestRange) {
        this.bestOccupiedRange = bestOccupiedRange;
        this.bestRange = bestRange;
    }

    public List<Integer> getBestOccupiedRange() {
        return bestOccupiedRange;
    }

    public void setBestOccupiedRange(List<Integer> bestOccupiedRange) {
        this.bestOccupiedRange = bestOccupiedRange;
    }

    public List<Integer> getBestRange() {
        return bestRange;
    }

    public void setBestRange(List<Integer> bestRange) {
        this.bestRange = bestRange;
    }

    @Override
    public String toString() {
        return "PremiumResponse{" +
                "bestOccupiedRange=" + bestOccupiedRange +
                ", bestRange=" + bestRange +
                '}';
    }
}
