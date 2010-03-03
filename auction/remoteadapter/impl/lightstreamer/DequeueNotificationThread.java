package auction.remoteadapter.impl.lightstreamer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.BlockingQueue;

public class DequeueNotificationThread implements Runnable {
	BlockingQueue<NotificationPacket> notificationQueue;
	BufferedWriter bWriter;

	public DequeueNotificationThread(
			BlockingQueue<NotificationPacket> notificationQueue,
			OutputStream oStream) {
		this.notificationQueue = notificationQueue;
		this.bWriter = new BufferedWriter(new OutputStreamWriter(oStream));
	}

	@Override
	public void run() {
		while (true) {
			try {
				NotificationPacket packet = notificationQueue.take();
				bWriter.write(packet.toString());
				bWriter.flush();
			} catch (InterruptedException ex) {

			} catch (IOException ex) {

			}
		}
	}
}
