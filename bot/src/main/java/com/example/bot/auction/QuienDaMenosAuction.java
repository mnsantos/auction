package com.example.bot.auction;

import com.example.bot.auction.model.BidResponse;
import com.example.bot.auction.model.PremiumResponse;
import com.example.bot.auction.model.quiendamenos.Bid;
import com.example.bot.auction.model.quiendamenos.BidRequest;
import com.example.bot.auction.model.quiendamenos.EndResponse;
import com.example.bot.auction.model.quiendamenos.Position;
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

public class QuienDaMenosAuction extends BaseAuction implements Auction {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private final String bidUrl;
    private final String bidsUrl;
    private final String premiumUrl;
    private final String statsUrl;
    private final String endUrl;
    private final String bearerAuth;
    private final RestTemplate restTemplate;

    public QuienDaMenosAuction(String auctionId, Integer bidCredits, Integer bestOccupiedCredits, Integer bestAvailableOrOccupiedCredits, String bearerAuth, String host) {
        super(auctionId, bidCredits, bestOccupiedCredits, bestAvailableOrOccupiedCredits);
        this.bearerAuth = bearerAuth;
        this.restTemplate = new RestTemplate();
        this.bidUrl = host + "/api/users/me/auctions/{auctionId}/bids";
        this.bidsUrl = host + "/api/users/me/auctions/{auctionId}/bids?start=1&end=600";
        this.premiumUrl = host + "/api/users/me/auctions/{auctionId}/bids/premium?pre={type}";
        this.statsUrl = host + "/api/users/me/auctions/{auctionId}/bids/graltable";
        this.endUrl = host + "/api/auctions/{auctionId}/uptime";
    }

    @Override
    public BidResponse bid(Integer cents) {
        BidRequest bidRequest = new BidRequest(new BigDecimal(cents).divide(ONE_HUNDRED));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(this.bearerAuth);
        HttpEntity<BidRequest> request = new HttpEntity<>(bidRequest, headers);
        com.example.bot.auction.model.quiendamenos.BidResponse bidResponse = restTemplate.postForEntity(bidUrl, request, com.example.bot.auction.model.quiendamenos.BidResponse.class).getBody();
        //TODO: check if it is valid
        return null;
    }

    @Override
    public List<String> stats() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.bearerAuth);
        List<Position> positions = restTemplate.exchange(statsUrl, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<Position>>() {
        }, auctionId).getBody();
        return positions.stream().map(Position::getUserNam).collect(Collectors.toList());
    }

    @Override
    public Set<BidResponse> bids() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.bearerAuth);
        HttpEntity<Object> request = new HttpEntity<>(null, headers);
        List<Bid> bids = restTemplate.exchange(bidsUrl, HttpMethod.GET, request, new ParameterizedTypeReference<List<Bid>>() {
        }).getBody();
        return bids.stream().map(b -> new BidResponse(getCents(b.getAmount()), b.getUnique() == 1, b.getPos())).collect(Collectors.toSet());
    }

    @Override
    public PremiumResponse bestOccupied() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.bearerAuth);
        //restTemplate.exchange(premiumUrl, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<>(){}, 2).getBody();
        //TODO: complete this
        return null;
    }

    @Override
    public PremiumResponse bestAvailableOrOccupied() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.bearerAuth);
        //restTemplate.exchange(premiumUrl, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<>(){}, 2).getBody();
        //TODO: complete this
        return null;
    }

    @Override
    public LocalDateTime endTime() {
        return restTemplate.getForEntity(endUrl, EndResponse.class, auctionId).getBody().getEndTimes().get(0).getDateTimeEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

    }

    private int getCents(BigDecimal a) {
        return a.multiply(ONE_HUNDRED).intValue();
    }
}
