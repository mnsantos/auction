package com.example.bot;

import com.example.bot.auction.Auction;
import com.example.bot.auction.QuienDaMenosAuction;
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

	/*@Override
	public void run(String... args) throws Exception {
		Auction simulationAuction = new SimulationAuction("2", 1, 20, 30, "mnsantos", "http://localhost:8080");
		SelfUpdateAuction selfUpdateAuction = new SelfUpdateAuction(simulationAuction);
		selfUpdateAuction.startListening();
		//AuctionBot bot = new SimpleBot(selfUpdateAuction, 100, 2);

		AuctionBot myBot = new SimpleBlockBot(selfUpdateAuction, 600, 1);
		myBot.run();
	}*/

	public void run(String... args) throws Exception {
		Auction q = new QuienDaMenosAuction("876",1,20,30,bearer,"https://www.quiendamenos.com");
		SelfUpdateAuction selfUpdateAuction = new SelfUpdateAuction(q);
		selfUpdateAuction.startListening();
		AuctionBot myBot = new SimpleBlockBot(selfUpdateAuction, 150, 2);
		myBot.run();
	}

	private static final String bearer = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZFVzZXIiOjQ3NDk5LCJzdGF0dXNVIjo0LCJ1c2VyTmFtZSI6Im1hdGkxIiwiZmlyc3ROYW1lIjoiIiwibGFzdE5hbWUiOiIiLCJlbWFpbCI6Im1hdGkxQGFybXlzcHkuY29tIiwicGFzc3dvcmQiOiIxMjM0NTYiLCJkYXRlQ3JlYXRlIjoiMjAxOS0wMy0xNFQwMDowMzo0NC4wMDBaIiwiZGF0ZUNvbmZpcm0iOiIyMDE5LTAzLTE0VDAwOjA5OjM1LjAwMFoiLCJkYXRlTGFzdEJpZCI6IjIwMTktMDMtMTRUMDA6MzU6NTMuMDAwWiIsImNvaW5zIjozLCJjb3VudFEiOjk1LCJoZWxwIjoxLCJub3RpZkdyYWwiOjEsIm1haWxpbmciOjEsIm5vdGlmQ29pbiI6MSwibm90aWZOb0NvaW4iOjEsInByb3hOb3RpIjowLCJjb3VudGVyRGF5cyI6MCwiZmFjZWJvb2tJZCI6bnVsbCwidHdpdHRlcklkIjpudWxsLCJzZXgiOm51bGwsImRuaSI6bnVsbCwiYmlyRGF0ZSI6IjAwMDAtMDAtMDAiLCJtb2JpbGVQcmVmaXgiOm51bGwsIm1vYmlsZU5ybyI6bnVsbCwibW9iaWxlQ29tcGFueUlkIjpudWxsLCJ0ZWxQcmVmaXgiOm51bGwsInRlbE5ybyI6bnVsbCwic3RyZWV0IjpudWxsLCJudW1iZXIiOm51bGwsImRlcHQiOm51bGwsImNpdHkiOiJuZXVxdWVuIiwiemlwIjoiIiwic3RhdGVJZCI6bnVsbCwiY291bnRyeSI6NTQsImxhbmRpbmciOm51bGwsIklQX0FkcmVzcyI6Ijo6ZmZmZjoxODEuMTExLjc0LjE5NiIsImVtYWlsX3ZhbGlkYXRpb25fa2V5IjpudWxsLCJlbWFpbF92YWxpZGF0aW9uX2tleV9leHBpcmF0aW9uIjoiMjAxOS0wMy0yMCAyMTowMjowNi41NDMiLCJwaG90b1R3IjoiIiwic2lndWllbmRvIjpudWxsLCJzZWd1aWRvcmVzIjpudWxsLCJwaG90b0ZiIjoiIiwiYWZpbGlhZG9yIjowLCJ1cmxBZmlsaWFkb3IiOjAsInNlY3JldCI6Im5zNTkzZ3N6aGVvdHdrcmFqdDR3dTd3MnlydDRoZXFwIiwiaWF0IjoxNTUyNjAyMjQyfQ.y47ch2gFAXspk7h8FCVEEfC8ALwdms2yE8QXpEiRkmg";
}
