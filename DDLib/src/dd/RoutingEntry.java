
package dd;


import java.net.InetAddress;

public class RoutingEntry {
	public InetAddress nextHop;
	public Integer metric;
	public Boolean isUp;
	
	public RoutingEntry(InetAddress nextHop, Integer metric, Boolean isUp) {
		super();
		this.nextHop = nextHop;
		this.metric = metric;
		this.isUp = isUp;
	}

	@Override
	public String toString() {
		return "[nextHop=" + nextHop + ", metric=" + metric
				+ ", isUp=" + isUp + "]";
	}

	
	
}
