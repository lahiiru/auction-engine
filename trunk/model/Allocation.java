package model;

public class Allocation {
	private String itemId;
	private double price;
	private String userId;

	public Allocation(String itemId, String userId, double price) {
		this.price = price;
		this.itemId = itemId;
		this.userId = userId;
	}

	public String getItemId() {
		return itemId;
	}

	public double getPrice() {
		return price;
	}

	public String getUserId() {
		return userId;
	}

}
