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
		JsonStatus status = new JsonStatus(proxyStatus);
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
		public String proxy;

		public JsonStatus(String proxy) {
			super();
			this.proxy = proxy;
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