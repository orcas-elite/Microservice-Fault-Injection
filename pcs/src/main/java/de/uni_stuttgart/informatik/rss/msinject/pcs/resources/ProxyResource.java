package de.uni_stuttgart.informatik.rss.msinject.pcs.resources;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Path("/proxy")
@Produces(MediaType.APPLICATION_JSON)
public class ProxyResource {
	private final Cache<String, Proxy> proxy_map;
	private final Client client;

	public ProxyResource(Client client, long duration) {
		this.client = client;
		this.proxy_map = CacheBuilder.newBuilder().expireAfterWrite(duration, TimeUnit.SECONDS).build();

	}

	@POST
	@Path("/{id}/delay")
	public Optional<Boolean> setDelayConfig(@PathParam("id") String id, ProxyDelayConfig delayConfig) {
		Proxy proxy = proxy_map.getIfPresent(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setDelayConfig(client, delayConfig));
	}

	@POST
	@Path("/{id}/drop")
	public Optional<Boolean> setDropConfig(@PathParam("id") String id, ProxyDropConfig dropConfig) {
		Proxy proxy = proxy_map.getIfPresent(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setDropConfig(client, dropConfig));
	}

	@POST
	@Path("/{id}/nlane")
	public Optional<Boolean> setNLaneConfig(@PathParam("id") String id, ProxyNLaneConfig nLaneConfig) {
		Proxy proxy = proxy_map.getIfPresent(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setNLaneConfig(client, nLaneConfig));
	}

	@POST
	@Path("/{id}/metrics")
	public Optional<Boolean> setNLaneConfig(@PathParam("id") String id, ProxyMetricsConfig metricsConfig) {
		Proxy proxy = proxy_map.getIfPresent(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setMetricsConfig(client, metricsConfig));
	}

	@GET
	@Path("/{id}/status")
	public Optional<ProxyStatus> getProxyStatus(@PathParam("id") String id) {
		Proxy proxy = proxy_map.getIfPresent(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.getProxyStatus(client));
	}

	@GET
	@Path("/{id}")
	public Optional<Proxy> getProxy(@PathParam("id") String id) {
		return Optional.ofNullable(proxy_map.getIfPresent(id));
	}

	@DELETE
	@Path("/{id}")
	public Optional<Proxy> delProxy(@PathParam("id") String id) {
		Proxy old = proxy_map.getIfPresent(id);
		proxy_map.invalidate(id);
		return Optional.ofNullable(old);
	}

	@PUT
	public Optional<Proxy> addProxy(@Context HttpServletRequest req, Proxy proxy) {
		String address = req.getRemoteAddr();
		proxy.setAddress(address);
		proxy_map.put(proxy.getId(), proxy);
		return Optional.of(proxy);
	}

	@GET
	public Map<String, Proxy> getProxies() {
		return proxy_map.asMap();
	}
}