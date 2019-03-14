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
		AuctionBot myBot = new SimpleBlockBot(selfUpdateAuction, 3, 70);
		myBot.run();
	}

	private static final String bearer = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZFVzZXIiOjQ3NTMxLCJzdGF0dXNVIjo0LCJ1c2VyTmFtZSI6IkRpc20xOTM2IiwiZmlyc3ROYW1lIjoiIiwibGFzdE5hbWUiOiIiLCJlbWFpbCI6IkRpc20xOTM2QGFybXlzcHkuY29tIiwicGFzc3dvcmQiOiIxMjM0NTYiLCJkYXRlQ3JlYXRlIjoiMjAxOS0wMy0xNFQyMjozMTowNS4wMDBaIiwiZGF0ZUNvbmZpcm0iOiIyMDE5LTAzLTE0VDIyOjMyOjI5LjAwMFoiLCJkYXRlTGFzdEJpZCI6IjAwMDAtMDAtMDAgMDA6MDA6MDAiLCJjb2lucyI6NSwiY291bnRRIjo5NSwiaGVscCI6MCwibm90aWZHcmFsIjoxLCJtYWlsaW5nIjoxLCJub3RpZkNvaW4iOjEsIm5vdGlmTm9Db2luIjoxLCJwcm94Tm90aSI6MCwiY291bnRlckRheXMiOjAsImZhY2Vib29rSWQiOm51bGwsInR3aXR0ZXJJZCI6bnVsbCwic2V4IjpudWxsLCJkbmkiOm51bGwsImJpckRhdGUiOiIwMDAwLTAwLTAwIiwibW9iaWxlUHJlZml4IjpudWxsLCJtb2JpbGVOcm8iOm51bGwsIm1vYmlsZUNvbXBhbnlJZCI6bnVsbCwidGVsUHJlZml4IjpudWxsLCJ0ZWxOcm8iOm51bGwsInN0cmVldCI6bnVsbCwibnVtYmVyIjpudWxsLCJkZXB0IjpudWxsLCJjaXR5IjoibmV1cXVlbiIsInppcCI6IiIsInN0YXRlSWQiOm51bGwsImNvdW50cnkiOjU0LCJsYW5kaW5nIjpudWxsLCJJUF9BZHJlc3MiOiI6OmZmZmY6MTgxLjExMS43NC4xOTYiLCJlbWFpbF92YWxpZGF0aW9uX2tleSI6bnVsbCwiZW1haWxfdmFsaWRhdGlvbl9rZXlfZXhwaXJhdGlvbiI6IjIwMTktMDMtMjEgMTk6Mjg6NDEuMTQ3IiwicGhvdG9UdyI6IiIsInNpZ3VpZW5kbyI6bnVsbCwic2VndWlkb3JlcyI6bnVsbCwicGhvdG9GYiI6IiIsImFmaWxpYWRvciI6NDc0OTksInVybEFmaWxpYWRvciI6NDc0OTksInNlY3JldCI6Im5zNTkzZ3N6aGVvdHdrcmFqdDR3dTd3MnlydDRoZXFwIiwiaWF0IjoxNTUyNjAyNjE0fQ.y9TcuIAyahAHc-JClCLiZxXfC7-xqUVO_f4GA96kxyM";
}
