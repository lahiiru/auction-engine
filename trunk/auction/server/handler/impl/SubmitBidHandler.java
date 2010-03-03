package auction.server.handler.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Bid;

import org.mortbay.jetty.*;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import auction.engine.EngineThread;

public class SubmitBidHandler extends org.mortbay.jetty.handler.AbstractHandler {
	private BlockingQueue<Bid> bidQueue;

	public SubmitBidHandler(BlockingQueue<Bid> bidQueue) {
		this.bidQueue = bidQueue;
	}

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, int dispatch) throws IOException,
			ServletException {
		if(!target.equals("/submitBid")){
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("");
			((Request) request).setHandled(true);
			return;
		}
		
		String itemId = request.getParameter("itemId");
		
		double price = 0;
		try{
			price = Double.parseDouble(request.getParameter("price"));
		} catch(NumberFormatException ex){
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}
		
		String userId = request.getParameter("userId");

		// push to bid queue
		try {
			bidQueue.put(new Bid(itemId, userId, price));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("");
		((Request) request).setHandled(true);
		
	}

}
