package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;
import com.example.bot.auction.model.simulation.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimulationAuction extends BaseAuction {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private final String bidUrl;
    private final String bidsUrl;
    private final String premiumUrl;
    private final String statsUrl;
    private final String endUrl;
    private final String userName;
    private final RestTemplate restTemplate;

    public SimulationAuction(String auctionId, Integer bidCredits, Integer bestOccupiedCredits, Integer bestAvailableOrOccupiedCredits, String userName, String host) {
        super(auctionId, bidCredits, bestOccupiedCredits, bestAvailableOrOccupiedCredits);
        this.userName = userName;
        this.restTemplate = new RestTemplate();
        this.bidUrl = host + "/auctions/bid";
        this.bidsUrl = host + "/auctions/bids";
        this.premiumUrl = host + "/auctions/premium?id={id}&type={type}";
        this.statsUrl = host + "/auctions/stats?id={id}";
        this.endUrl = host + "/auctions/end?id={id}";
    }

    @Override
    public BidResponse bid(Integer cents) {
        User user = new User(userName);
        BidRequest bidRequest = new BidRequest(user, new BigDecimal(cents).divide(ONE_HUNDRED), Integer.valueOf(this.auctionId));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BidRequest> request = new HttpEntity<>(bidRequest, headers);
        BidResponse bidResponse = restTemplate.postForEntity(bidUrl, request, BidResponse.class).getBody();
        bidResponse.setAmount(cents);
        return bidResponse;
    }

    @Override
    public Set<BidResponse> bids() {
        User user = new User(userName);
        BidsRequest bidsRequest = new BidsRequest(user, Integer.valueOf(auctionId));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BidsRequest> request = new HttpEntity<>(bidsRequest, headers);
        return restTemplate.exchange(bidsUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Set<BidResponse>>() {
        }).getBody();
    }

    @Override
    public PremiumResponse bestOccupied() {
        return getPremiumResponse(restTemplate.getForEntity(premiumUrl, com.example.bot.auction.model.simulation.PremiumResponse.class, this.auctionId, "2").getBody());
    }

    @Override
    public PremiumResponse bestAvailableOrOccupied() {
        return getPremiumResponse(restTemplate.getForEntity(premiumUrl, com.example.bot.auction.model.simulation.PremiumResponse.class, this.auctionId, "4").getBody());
    }

    @Override
    public List<String> stats() {
        List<Position> positions = restTemplate.exchange(statsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Position>>() {
        }, auctionId).getBody();
        return positions.stream().map(p -> p.getUser().getName()).collect(Collectors.toList());
    }

    @Override
    public LocalDateTime endTime() {
        return restTemplate.getForEntity(endUrl, EndResponse.class, auctionId).getBody().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private PremiumResponse getPremiumResponse(com.example.bot.auction.model.simulation.PremiumResponse response) {
        List<Integer> bestOccupiedRange = response.getBestOccupiedRange() != null ? response.getBestOccupiedRange().stream().map(this::getCents).collect(Collectors.toList()) : null;
        List<Integer> bestRange = response.getBestRange() != null ? response.getBestRange().stream().map(this::getCents).collect(Collectors.toList()) : null;
        return new PremiumResponse(bestOccupiedRange, bestRange);
    }

    private int getCents(BigDecimal a) {
        return a.multiply(ONE_HUNDRED).intValue();
    }
}
