package auction;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import model.Allocation;
import model.Bid;

import auction.engine.Engine;
import auction.engine.EngineThread;
import auction.engine.impl.DummyEngine;
import auction.remoteadapter.RemoteDataAdapter;
import auction.remoteadapter.impl.lightstreamer.LightStreamerAdapter;
import auction.server.ServerThread;

public class AuctionApplication {
	private static BlockingQueue<Bid> bidQueue;
	public static final int MAX_QUEUE_SIZE = 1000;
	public static void main(String[] args) throws Exception {
		bidQueue = new ArrayBlockingQueue<Bid>(MAX_QUEUE_SIZE);
		Engine engine = new DummyEngine();
		RemoteDataAdapter remoteDataAdapter = new LightStreamerAdapter(
				"localhost", 7001, 7002);
		remoteDataAdapter.start();
		new ServerThread(Integer.parseInt(args[0]), bidQueue).start();
		new EngineThread(bidQueue, engine, remoteDataAdapter).start();
	}
}
