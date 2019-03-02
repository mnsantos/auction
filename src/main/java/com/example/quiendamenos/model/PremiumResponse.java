package com.example.quiendamenos.model;

import java.util.List;

public class PremiumResponse {
    private List<Double> bestOccupiedRange;
    private List<Double> bestPlaceRange;

    public PremiumResponse() {
    }

    public PremiumResponse(List<Double> bestOccupiedRange, List<Double> bestPlaceRange) {
        this.bestOccupiedRange = bestOccupiedRange;
        this.bestPlaceRange = bestPlaceRange;
    }

    public List<Double> getBestOccupiedRange() {
        return bestOccupiedRange;
    }

    public void setBestOccupiedRange(List<Double> bestOccupiedRange) {
        this.bestOccupiedRange = bestOccupiedRange;
    }

    public List<Double> getBestPlaceRange() {
        return bestPlaceRange;
    }

    public void setBestPlaceRange(List<Double> bestPlaceRange) {
        this.bestPlaceRange = bestPlaceRange;
    }
}
