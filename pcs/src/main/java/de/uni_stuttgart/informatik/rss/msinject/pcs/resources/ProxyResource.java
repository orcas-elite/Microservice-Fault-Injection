package de.uni_stuttgart.informatik.rss.msinject.pcs.resources;

import de.uni_stuttgart.informatik.rss.msinject.pcs.models.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Path("/proxy")
@Produces(MediaType.APPLICATION_JSON)
public class ProxyResource {
	private final ConcurrentMap<String, Proxy> proxy_map = new ConcurrentHashMap<>();

	private final Client client;

	public ProxyResource(Client client) {
		this.client = client;
	}

	@POST
	@Path("/{id}/delay")
	public Optional<Boolean> setDelayConfig(@PathParam("id") String id, ProxyDelayConfig delayConfig) {
		Proxy proxy = proxy_map.get(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setDelayConfig(client, delayConfig));
	}

	@POST
	@Path("/{id}/drop")
	public Optional<Boolean> setDropConfig(@PathParam("id") String id, ProxyDropConfig dropConfig) {
		Proxy proxy = proxy_map.get(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setDropConfig(client, dropConfig));
	}

	@POST
	@Path("/{id}/nlane")
	public Optional<Boolean> setNLaneConfig(@PathParam("id") String id, ProxyNLaneConfig nLaneConfig) {
		Proxy proxy = proxy_map.get(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setNLaneConfig(client, nLaneConfig));
	}

	@POST
	@Path("/{id}/metrics")
	public Optional<Boolean> setNLaneConfig(@PathParam("id") String id, ProxyMetricsConfig metricsConfig) {
		Proxy proxy = proxy_map.get(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setMetricsConfig(client, metricsConfig));
	}

	@GET
	@Path("/{id}/status")
	public Optional<ProxyStatus> getProxyStatus(@PathParam("id") String id) {
		Proxy proxy = proxy_map.get(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.getProxyStatus(client));
	}

	@GET
	@Path("/{id}")
	public Optional<Proxy> getProxy(@PathParam("id") String id) {
		return Optional.of(proxy_map.get(id));
	}

	@DELETE
	@Path("/{id}")
	public Optional<Proxy> delProxy(@PathParam("id") String id) {
		return Optional.of(proxy_map.remove(id));
	}

	@PUT
	public Optional<Proxy> addProxy(@Context HttpServletRequest req, Proxy proxy) {
		String address = req.getRemoteAddr();
		proxy.setAddress(address);
		return Optional.of(proxy_map.put(proxy.getId(), proxy));
	}

	@GET
	public Map<String, Proxy> getProxies() {
		return proxy_map;
	}
}