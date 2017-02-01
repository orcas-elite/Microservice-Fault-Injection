package proxycontrol;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyDropConfig;
import proxy.Proxy;


@Path("control")
public class ProxyControl {
	public static Proxy proxy;

	@GET
	@Path("status")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStatus() {
		return new Gson().toJson(proxy.getStatus());
	}

	
	@GET
	@Path("drop")
	@Consumes(MediaType.APPLICATION_JSON)
	public String getDrop() {
		return new Gson().toJson(proxy.getDropConfig());
	}

	@POST
	@Path("drop")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postDrop(final String input) {
		ProxyDropConfig config = new Gson().fromJson(input, ProxyDropConfig.class);
		proxy.setDropConfig(config);
		return new Gson().toJson(proxy.getDropConfig());
	}
	

	@POST
	@Path("delay")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postDelay(final String input) {
		JsonSetDelay json = new Gson().fromJson(input, JsonSetDelay.class);
		proxy.setDelay(json.enabled, json.probability, json.min, json.max);
		return "Success";
	}

	@POST
	@Path("nlane")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postNLane(final String input) {
		JsonSetNLane json = new Gson().fromJson(input, JsonSetNLane.class);
		proxy.setMaxActiveNLaneBridge(json.enabled, json.maxActive);
		return "Success";
	}

	@POST
	@Path("metrics")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postMetrics(final String input) {
		JsonSetMetrics json = new Gson().fromJson(input, JsonSetMetrics.class);
		proxy.setMetrics(json.enabled);
		return "Success";
	}
	

	@SuppressWarnings("unused")
	private class JsonStatus {
		public String proxyStatus;
		public long requestsServiced = 0;
		public long requestsDelayed = 0;
		public long requestsDropped = 0;

		public JsonStatus(String proxyStatus, long requestsServiced, long requestsDelayed, long requestsDropped) {
			super();
			this.proxyStatus = proxyStatus;
			this.requestsServiced = requestsServiced;
			this.requestsDelayed = requestsDelayed;
			this.requestsDropped = requestsDropped;
		}
	}
	

	private class JsonSetDelay {
		public boolean enabled;
		public float probability;
		public int min;
		public int max;
	}
	

	private class JsonSetNLane {
		public boolean enabled;
		public int maxActive;
	}
	

	private class JsonSetMetrics {
		public boolean enabled;
	}
}