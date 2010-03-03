package auction.remoteadapter.impl.lightstreamer;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import model.Allocation;

import java.util.HashMap;

import auction.AuctionApplication;
import auction.remoteadapter.RemoteDataAdapter;

public class LightStreamerAdapter implements RemoteDataAdapter {
	private Socket rrSocket, notificationSocket;
	private int rrPort, notificationPort;
	private String hostName;

	private BlockingQueue<RRPacket> requestQueue;
	private BlockingQueue<RRPacket> replyQueue;
	private BlockingQueue<NotificationPacket> notificationQueue;
	private Map<String, String> sessionIdMap;
	private Thread dequeueNotificationThread, dequeueReplyThread, deuqueRequestThread, enqueueRequestThread;
	public LightStreamerAdapter(String hostName, int rrPort,
			int notificationPort) throws UnknownHostException, IOException {
		this.rrPort = rrPort;
		this.notificationPort = notificationPort;
		this.hostName = hostName;
		rrSocket = new Socket(hostName, rrPort);
		notificationSocket = new Socket(hostName, notificationPort);
		replyQueue = new ArrayBlockingQueue<RRPacket>(AuctionApplication.MAX_QUEUE_SIZE);
		requestQueue = new ArrayBlockingQueue<RRPacket>(AuctionApplication.MAX_QUEUE_SIZE);
		notificationQueue = new ArrayBlockingQueue<NotificationPacket>(
				AuctionApplication.MAX_QUEUE_SIZE);
		sessionIdMap = new HashMap<String, String>();
		
		//setting up threads
		this.dequeueNotificationThread = 
			new Thread(new DequeueNotificationThread(notificationQueue, notificationSocket.getOutputStream()));
		this.dequeueReplyThread = 
			new Thread(new DequeueReplyThread(replyQueue, rrSocket.getOutputStream()));
		this.deuqueRequestThread = 
			new Thread(new DequeueRequestThread(requestQueue, replyQueue, sessionIdMap));
		this.enqueueRequestThread = 
			new Thread(new EnqueueRequestThread(requestQueue, rrSocket.getInputStream()));
	}
	
	public void start(){
		try {
			dequeueNotificationThread.start();
			dequeueReplyThread.start();
			deuqueRequestThread.start();
			enqueueRequestThread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void send(Allocation allocation){
		List<DataSegment> data = new ArrayList<DataSegment>();
		String sessionId = "";
		synchronized(this){
			sessionId = sessionIdMap.get(allocation.getItemId());
		}
		//Item id
		data.add(new DataSegment(DataType.S, allocation.getItemId()));
		//Unique ID
		data.add(new DataSegment(DataType.S, sessionId));
		//Snapshot
		data.add(new DataSegment(DataType.B, "0"));
		//allocation data
		data.add(new DataSegment(DataType.S, "userId"));
		data.add(new DataSegment(DataType.S, allocation.getUserId()));
		data.add(new DataSegment(DataType.S, "price"));
		data.add(new DataSegment(DataType.S,((Double)allocation.getPrice()).toString()));
		NotificationPacket packet = new NotificationPacket(System.currentTimeMillis(), Method.UD3, data);
		try {
			notificationQueue.put(packet);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
