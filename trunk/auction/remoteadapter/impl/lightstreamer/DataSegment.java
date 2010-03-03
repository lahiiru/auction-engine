package auction.remoteadapter.impl.lightstreamer;

public class DataSegment {
	private DataType dataType;
	private String data;

	public DataSegment(DataType dataType, String data) {
		this.dataType = dataType;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public DataType getDataType() {
		return dataType;
	}

}
