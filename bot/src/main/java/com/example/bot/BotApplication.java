package com.example.bot;

import com.example.bot.auction.SelfUpdateAuction;
import com.example.bot.auction.SimulationAuction;
import com.example.bot.bot.AuctionBot;
import com.example.bot.bot.ReactiveBotImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BotApplication implements CommandLineRunner {

	private static Logger LOG = LoggerFactory
			.getLogger(BotApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		SelfUpdateAuction auction = new SimulationAuction(2, 20, 30, "mnsantos", "http://localhost:8080", "2");
		auction.startListening();
		AuctionBot bot = new ReactiveBotImpl(auction, 100, 2);
		auction.addObserver(bot);
		bot.run();
	}
}
