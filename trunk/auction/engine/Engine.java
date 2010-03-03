package auction.engine;

import model.Allocation;
import model.Bid;

public interface Engine {
	public Allocation compute(Bid bid);
}
