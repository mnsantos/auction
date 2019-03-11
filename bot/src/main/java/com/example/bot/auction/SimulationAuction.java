package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;
import com.example.bot.auction.model.simulation.BidRequest;
import com.example.bot.auction.model.simulation.Position;
import com.example.bot.auction.model.simulation.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimulationAuction extends BaseAuction {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private final String bidUrl;
    private final String premiumUrl;
    private final String statsUrl;
    private final String userName;
    private final RestTemplate restTemplate;

    public SimulationAuction(Integer bidCredits, Integer bestOccupiedCredits, Integer bestAvailableOrOccupiedCredits, String userName, String host, String auctionId) {
        super(bidCredits, bestOccupiedCredits, bestAvailableOrOccupiedCredits, auctionId);
        this.userName = userName;
        this.restTemplate = new RestTemplate();
        this.bidUrl = host + "/auctions/bid";
        this.premiumUrl = host + "/auctions/premium?id={id}&type={type}";
        this.statsUrl = host + "/auctions/stats?id={id}";
    }

    @Override
    public BidResponse makeBid(Integer cents) {
        User user = new User(userName);
        BidRequest bidRequest = new com.example.bot.auction.model.simulation.BidRequest(user, new BigDecimal(cents).divide(ONE_HUNDRED), Integer.valueOf(this.auctionId));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BidRequest> request = new HttpEntity<>(bidRequest, headers);
        com.example.bot.auction.model.simulation.BidResponse bidResponse = restTemplate.postForEntity(bidUrl, request, com.example.bot.auction.model.simulation.BidResponse.class).getBody();
        return new BidResponse(cents, bidResponse.isValid(), bidResponse.getPosition(), bidResponse.getPositionDisplaced());
    }

    @Override
    protected Set<BidResponse> getBidsMade() {
        return null;
    }

    @Override
    public PremiumResponse getBestOccupied() {
        return getPremiumResponse(restTemplate.getForEntity(premiumUrl, com.example.bot.auction.model.simulation.PremiumResponse.class, this.auctionId, "2").getBody());
    }

    @Override
    public PremiumResponse getBestAvailableOrOccupied() {
        return getPremiumResponse(restTemplate.getForEntity(premiumUrl, com.example.bot.auction.model.simulation.PremiumResponse.class, this.auctionId, "4").getBody());
    }

    @Override
    public Integer getActualPosition() {
        return null;
    }

    @Override
    public List<String> stats() {
        List<Position> positions = restTemplate.exchange(statsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Position>>() {
        }).getBody();
        return positions.stream().map(p -> p.getUser().getName()).collect(Collectors.toList());
    }

    @Override
    public LocalDateTime endTime() {
        return null;
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
