package auction.remoteadapter.impl.lightstreamer;

import java.util.ArrayList;
import java.util.List;

public class RRPacket {
	private String id;
	private Method method;
	private List<DataSegment> data;

	public RRPacket(String id, Method method, List<DataSegment> data) {
		this.id = id;
		this.method = method;
		this.data = data;
	}

	public RRPacket(String id, Method method, DataSegment dataSegment) {
		this.id = id;
		this.method = method;
		this.data = new ArrayList<DataSegment>();
		this.data.add(dataSegment);
	}

	public static RRPacket parse(String str) {
		String[] fields = str.split("\\|");
		String id = fields[0];
		Method method = Method.valueOf(fields[1]);
		List<DataSegment> data = new ArrayList<DataSegment>();
		for (int i = 2; i < fields.length; i += 2) {
			if (fields[i] == "V") {
				data.add(new DataSegment(DataType.V, ""));
				i--;
			} else {
				data.add(new DataSegment(DataType.valueOf(fields[i]),
						fields[i + 1]));
			}
		}
		return new RRPacket(id, method, data);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.id + "|");
		sb.append(this.method + "|");
		for (int i = 0; i < this.data.size(); i++) {
			if (data.get(i).getDataType() == DataType.V) {
				sb.append("V|");
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

	public String getId() {
		return id;
	}

	public Method getMethod() {
		return method;
	}

	List<DataSegment> getData() {
		return data;
	}
}
