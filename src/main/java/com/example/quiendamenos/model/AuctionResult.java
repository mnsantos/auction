package com.example.quiendamenos.model;

import java.util.List;

public class AuctionResult {
    private List<Position> stats;
    private List<Cell> cells;

    public List<Position> getStats() {
        return stats;
    }

    public void setStats(List<Position> stats) {
        this.stats = stats;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
