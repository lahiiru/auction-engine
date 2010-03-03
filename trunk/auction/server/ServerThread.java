package auction.server;

import java.util.concurrent.BlockingQueue;

import model.Bid;

import org.mortbay.jetty.Server;

import auction.AuctionApplication;
import auction.server.handler.impl.SubmitBidHandler;

public class ServerThread extends Thread {
	private Server jettyServer;
	private static AuctionApplication app;
	private BlockingQueue<Bid> bidQueue;

	public ServerThread(int httpPort, BlockingQueue<Bid> bidQueue) {
		this.bidQueue = bidQueue;
		this.jettyServer = new Server(httpPort);
		this.jettyServer.addHandler(new SubmitBidHandler(bidQueue));
	}

	public void start() {
		try {
			jettyServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
