package auction.engine;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import model.Allocation;
import model.Bid;

import auction.engine.impl.DummyEngine;
import auction.remoteadapter.RemoteDataAdapter;

public class EngineThread extends Thread {
	Engine auctionEngine;
	RemoteDataAdapter remoteDataAdapter;
	BlockingQueue<Bid> bidQueue;

	public EngineThread(BlockingQueue<Bid> bidQueue, Engine auctionEngine,
			RemoteDataAdapter adapter) {
		this.remoteDataAdapter = adapter;
		this.auctionEngine = auctionEngine;
		this.bidQueue = bidQueue;
	}

	public void start() {
		while (true) {
			try {
				Allocation alloc = auctionEngine.compute(bidQueue.take());
				remoteDataAdapter.send(alloc);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
}
