package com.example.bot;

import com.example.bot.auction.Auction;
import com.example.bot.auction.SelfUpdateAuction;
import com.example.bot.auction.SimulationAuction;
import com.example.bot.bot.AuctionBot;
import com.example.bot.bot.SimpleBlockBot;
import com.example.bot.bot.SimpleBot;
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
		Auction simulationAuction = new SimulationAuction("mnsantos", "http://localhost:8080", "2");
		SelfUpdateAuction selfUpdateAuction = new SelfUpdateAuction(2, 20, 30, simulationAuction);
		selfUpdateAuction.startListening();
		AuctionBot bot = new SimpleBot(selfUpdateAuction, 100, 2);
		//bot.run();

		AuctionBot myBot = new SimpleBlockBot(selfUpdateAuction, 600, 1);
		myBot.run();
	}
}
