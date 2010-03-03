package auction.remoteadapter.impl.lightstreamer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.BlockingQueue;

public class DequeueReplyThread implements Runnable {
	BlockingQueue<RRPacket> replyQueue;
	BufferedWriter bWriter;

	public DequeueReplyThread(BlockingQueue<RRPacket> replyQueue,
			OutputStream oStream) {
		this.replyQueue = replyQueue;
		this.bWriter = new BufferedWriter(new OutputStreamWriter(oStream));
	}

	@Override
	public void run() {
		while (true) {
			try {
				RRPacket reply = replyQueue.take();
				bWriter.write(reply.toString());
				bWriter.flush();
			} catch (InterruptedException ex) {

			} catch (IOException ex) {

			}
		}

	}

}
