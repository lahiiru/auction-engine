package auction.remoteadapter;

import model.Allocation;

public interface RemoteDataAdapter {
	public void start();
	public void send(Allocation allocation);
}
