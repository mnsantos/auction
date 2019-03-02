package com.example.quiendamenos.controller;

import com.example.quiendamenos.model.*;
import com.example.quiendamenos.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("auctions")
@RestController
public class AuctionController {

    private final static BigDecimal TEN_CENTS = new BigDecimal("0.1");
    @Autowired
    private AuctionService auctionService;

    @PostMapping(value = "/bid")
    public ResponseEntity<BidResponse> bid(@RequestBody BidRequest request) {
        return new ResponseEntity<>(auctionService.bid(request), HttpStatus.OK);
    }

    @GetMapping(value = "/start")
    public ResponseEntity<Map<String, Integer>> start(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date timeLimit) {
        Map<String, Integer> response = new HashMap<>();
        int id = this.auctionService.start(timeLimit);
        response.put("id", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/finish")
    public ResponseEntity<Object> finish(@RequestParam int id) {
        this.auctionService.stop(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/extend")
    public ResponseEntity<Object> extend(@RequestParam int id, @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date newTimeLimit) {
        this.auctionService.extend(id, newTimeLimit);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/stats")
    public ResponseEntity<List<Position>> stats(@RequestParam int id) {
        return new ResponseEntity<>(auctionService.stats(id), HttpStatus.OK);
    }

    @GetMapping(value = "/results")
    public ResponseEntity<AuctionResult> results(@RequestParam int id) {
        return new ResponseEntity<>(auctionService.result(id), HttpStatus.OK);
    }

    @GetMapping(value = "/premium")
    public ResponseEntity<PremiumResponse> premium(@RequestParam int id, @RequestParam int type) {
        PremiumResponse premiumResponse;
        switch (type) {
            case (1):
                premiumResponse = auctionService.bestOccupied(id, BigDecimal.ONE);
                break;
            case (2):
                premiumResponse = auctionService.bestOccupied(id, TEN_CENTS);
                break;
            case (3):
                premiumResponse = auctionService.bestOrOccupied(id, BigDecimal.ONE);
                break;
            case (4):
                premiumResponse = auctionService.bestOrOccupied(id, TEN_CENTS);
                break;
            default:
                throw new RuntimeException(String.format("Unrecognized type %s", type));
        }
        return new ResponseEntity<>(premiumResponse, HttpStatus.OK);
    }
}
