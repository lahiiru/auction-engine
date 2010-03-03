package model;

public class Bid {
	private String itemId;
	private double price;
	private String userId;

	public Bid(String itemId, String userId, double price) {
		this.itemId = itemId;
		this.price = price;
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}
	
	public String getItemId() {
		return itemId;
	}

	public double getPrice() {
		return price;
	}
}
