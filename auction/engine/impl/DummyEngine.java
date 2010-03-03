package auction.engine.impl;

import java.util.Random;

import model.Allocation;
import model.Bid;

import auction.engine.Engine;
import auction.engine.EngineThread;

public class DummyEngine implements Engine {

	Random rand = new Random();

	@Override
	public Allocation compute(Bid bid) {
		return new Allocation(bid.getItemId(), "Trung Vu", bid.getPrice() + 5);
	}

}
