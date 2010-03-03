package auction.remoteadapter.impl.lightstreamer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class EnqueueRequestThread implements Runnable {
	BlockingQueue<RRPacket> requestQueue;
	BufferedReader bReader;

	public EnqueueRequestThread(BlockingQueue<RRPacket> requestQueue,
			InputStream inputStream) {
		this.bReader = new BufferedReader(new InputStreamReader(inputStream));
		this.requestQueue = requestQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String msg;
				while ((msg = bReader.readLine()) == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				requestQueue.put(RRPacket.parse(msg));
			} catch (Exception ex) {
				continue;
			}
		}
	}
}
