package de.uni_stuttgart.informatik.rss.msinject.pcs.resources;

import com.google.common.base.Optional;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/tag")
@Produces(MediaType.APPLICATION_JSON)
public class TagResource {

	private final ProxyResource proxyResource;

	public TagResource(ProxyResource proxyResource) {
		this.proxyResource = proxyResource;
	}

	@POST
	@Path("/{tag}/delay")
	public Map<String, Boolean> setDelayConfig(@PathParam("tag") String tag, ProxyDelayConfig delayConfig) {

		Stream<Proxy> proxies = proxyResource.getByTag(tag);

		return proxies.collect(Collectors.toMap(Proxy::getUuid, p -> {
			Optional<Boolean> r = proxyResource.setDelayConfig(p.getUuid(), delayConfig);
			return r.or(false);
		}));
	}

	@POST
	@Path("/{tag}/drop")
	public Map<String, Boolean> setDropConfig(@PathParam("tag") String tag, ProxyDropConfig dropConfig) {

		Stream<Proxy> proxies = proxyResource.getByTag(tag);

		return proxies.collect(Collectors.toMap(Proxy::getUuid, p -> {
			Optional<Boolean> r = proxyResource.setDropConfig(p.getUuid(), dropConfig);
			return r.or(false);
		}));
	}

	@POST
	@Path("/{tag}/nlane")
	public Map<String, Boolean> setNLaneConfig(@PathParam("tag") String tag, ProxyNLaneConfig nLaneConfig) {
		Stream<Proxy> proxies = proxyResource.getByTag(tag);

		return proxies.collect(Collectors.toMap(Proxy::getUuid, p -> {
			Optional<Boolean> r = proxyResource.setNLaneConfig(p.getUuid(), nLaneConfig);
			return r.or(false);
		}));
	}

	@POST
	@Path("/{tag}/metrics")
	public Map<String, Boolean> setMetricsConfig(@PathParam("tag") String tag, ProxyMetricsConfig metricsConfig) {
		Stream<Proxy> proxies = proxyResource.getByTag(tag);

		return proxies.collect(Collectors.toMap(Proxy::getUuid, p -> {
			Optional<Boolean> r = proxyResource.setMetricsConfig(p.getUuid(), metricsConfig);
			return r.or(false);
		}));
	}
}
