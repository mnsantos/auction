package com.example.quiendamenos.model;

import java.math.BigDecimal;
import java.util.List;

public class PremiumResponse {
    private List<BigDecimal> bestOccupiedRange;
    private List<BigDecimal> bestRange;

    public PremiumResponse() {
    }

    public PremiumResponse(List<BigDecimal> bestOccupiedRange, List<BigDecimal> bestRange) {
        this.bestOccupiedRange = bestOccupiedRange;
        this.bestRange = bestRange;
    }

    public List<BigDecimal> getBestOccupiedRange() {
        return bestOccupiedRange;
    }

    public void setBestOccupiedRange(List<BigDecimal> bestOccupiedRange) {
        this.bestOccupiedRange = bestOccupiedRange;
    }

    public List<BigDecimal> getBestRange() {
        return bestRange;
    }

    public void setBestRange(List<BigDecimal> bestRange) {
        this.bestRange = bestRange;
    }
}
