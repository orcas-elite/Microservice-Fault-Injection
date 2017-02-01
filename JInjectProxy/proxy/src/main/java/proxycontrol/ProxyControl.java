package proxycontrol;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyDelayConfig;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyDropConfig;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyMetricsConfig;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyNLaneConfig;
import proxy.Proxy;


@Path("control")
public class ProxyControl {
	public static Proxy proxy;

	@GET
	@Path("status")
	public String getStatus() {
		return new Gson().toJson(proxy.getStatus());
	}

	
	@GET
	@Path("drop")
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
	

	@GET
	@Path("delay")
	public String getDelay() {
		return new Gson().toJson(proxy.getDelayConfig());
	}
	
	@POST
	@Path("delay")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postDelay(final String input) {
		ProxyDelayConfig config = new Gson().fromJson(input, ProxyDelayConfig.class);
		proxy.setDelayConfig(config);
		return new Gson().toJson(proxy.getDelayConfig());
	}
	

	@GET
	@Path("nlane")
	public String getNLane() {
		return new Gson().toJson(proxy.getNLaneBridgeConfig());
	}
	
	@POST
	@Path("nlane")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postNLane(final String input) {
		ProxyNLaneConfig config = new Gson().fromJson(input, ProxyNLaneConfig.class);
		proxy.setNLaneBridgeConfig(config);
		return new Gson().toJson(proxy.getNLaneBridgeConfig());
	}
	
	
	@GET
	@Path("metrics")
	public String getMetrics() {
		return new Gson().toJson(proxy.getMetricsConfig());
	}
	
	@POST
	@Path("metrics")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postMetrics(final String input) {
		ProxyMetricsConfig config = new Gson().fromJson(input, ProxyMetricsConfig.class);
		proxy.setMetricsConfig(config);
		return new Gson().toJson(proxy.getMetricsConfig());
	}
}