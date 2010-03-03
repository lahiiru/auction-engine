package auction.remoteadapter.impl.lightstreamer;

import java.util.ArrayList;
import java.util.List;

public class NotificationPacket {
	private String timeStamp;
	private Method method;
	private List<DataSegment> data;

	public NotificationPacket(Long timeStamp, Method method,
			List<DataSegment> data) {
		this.timeStamp = timeStamp.toString();
		this.method = method;
		this.data = data;
	}

	public static NotificationPacket parse(String str) {
		String[] fields = str.split("\\|");
		String timeStamp = fields[0];
		Method method = Method.valueOf(fields[1]);
		List<DataSegment> data = new ArrayList<DataSegment>();
		for (int i = 2; i < fields.length; i += 2) {
			if (fields[i] == "V") {
				data.add(new DataSegment(DataType.V, ""));
			} else {
				data.add(new DataSegment(DataType.valueOf(fields[i]),
						fields[i + 1]));
			}
		}
		return new NotificationPacket(Long.parseLong(timeStamp), method, data);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.timeStamp + "|");
		sb.append(this.method + "|");
		for (int i = 0; i < this.data.size(); i++) {
			if (data.get(i).getDataType() == DataType.V) {
				sb.append("V|");
				i--;
			} else {
				sb.append(data.get(i).getDataType() + "|"
						+ data.get(i).getData() + "|");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append('\r');
		sb.append('\n');
		return sb.toString();
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public Method getMethod() {
		return this.method;
	}

	List<DataSegment> getData() {
		return data;
	}
}
