package proxycontrol;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import proxy.Proxy;


@Path("control")
public class ProxyControl {
	public static Proxy proxy;

	@GET
	@Path("status")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStatus() {
		String proxyStatus = proxy != null ? (proxy.isStarted() ? "started" : "not started") : "null";
		JsonStatus status = new JsonStatus(proxyStatus, proxy.getRequestsServiced(), proxy.getRequestsDelayed(), proxy.getRequestsDropped());
		return new Gson().toJson(status);
	}

	@POST
	@Path("set/drop")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postDrop(final String input) {
		JsonSetDrop json = new Gson().fromJson(input, JsonSetDrop.class);
		proxy.setDrop(json.enabled, json.probability);
		return "Success";
	}

	@POST
	@Path("set/delay")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postDelay(final String input) {
		JsonSetDelay json = new Gson().fromJson(input, JsonSetDelay.class);
		proxy.setDelay(json.enabled, json.probability, json.min, json.max);
		return "Success";
	}
	

	@SuppressWarnings("unused")
	private class JsonStatus {
		public String proxyStatus;
		public int requestsServiced = 0;
		public int requestsDelayed = 0;
		public int requestsDropped = 0;

		public JsonStatus(String proxyStatus, int requestsServiced, int requestsDelayed, int requestsDropped) {
			super();
			this.proxyStatus = proxyStatus;
			this.requestsServiced = requestsServiced;
			this.requestsDelayed = requestsDelayed;
			this.requestsDropped = requestsDropped;
		}
	}
	

	private class JsonSetDrop {
		public boolean enabled;
		public double probability;
	}
	

	private class JsonSetDelay {
		public boolean enabled;
		public double probability;
		public int min;
		public int max;
	}
}