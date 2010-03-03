package auction.remoteadapter.impl.lightstreamer;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class DequeueRequestThread implements Runnable {
	BlockingQueue<RRPacket> requestQueue, replyQueue;
	Map<String, String> sessionIDMap;

	public DequeueRequestThread(BlockingQueue<RRPacket> requestQueue,
			BlockingQueue<RRPacket> replyQueue, Map<String, String> sessionIDMap) {
		this.replyQueue = replyQueue;
		this.requestQueue = requestQueue;
		this.sessionIDMap = sessionIDMap;
	}

	@Override
	public void run() {
		while (true) {
			try {
				RRPacket reply = reply(requestQueue.take());
				replyQueue.put(reply);
			} catch (InterruptedException ex) {

			}
		}
	}

	private RRPacket reply(RRPacket request) {
		Method method = request.getMethod();
		RRPacket reply = null;
		switch (method) {
		case SUB:
			String itemId = request.getData().get(0).getData();
			synchronized (this) {
				sessionIDMap.put(itemId, request.getId());
			}
			reply = new RRPacket(request.getId(), Method.SUB, new DataSegment(
					DataType.V, ""));

			break;
		}
		return reply;
	}
}
