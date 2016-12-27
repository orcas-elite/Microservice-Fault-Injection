package de.uni_stuttgart.informatik.rss.msinject.pcs.resources;

import de.uni_stuttgart.informatik.rss.msinject.pcs.models.Proxy;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyDelayConfig;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyDropConfig;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyStatus;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Path("/proxy")
@Produces(MediaType.APPLICATION_JSON)
public class ProxyResource {
	private final AtomicLong counter = new AtomicLong();
	private final ConcurrentMap<Long, Proxy> proxy_map = new ConcurrentHashMap<>();

	private final Client client;

	public ProxyResource(Client client) {
		this.client = client;
	}

	@POST
	@Path("/{id}/delay")
	public Optional<Boolean> setDelayConfig(@PathParam("id") long id, ProxyDelayConfig delayConfig) {
		Proxy proxy = proxy_map.get(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setDelayConfig(client, delayConfig));
	}

	@POST
	@Path("/{id}/drop")
	public Optional<Boolean> setDropConfig(@PathParam("id") long id, ProxyDropConfig dropConfig) {
		Proxy proxy = proxy_map.get(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.setDropConfig(client, dropConfig));
	}

	@GET
	@Path("/{id}/status")
	public Optional<ProxyStatus> getProxyStatus(@PathParam("id") long id) {
		Proxy proxy = proxy_map.get(id);

		if (proxy == null) {
			return Optional.empty();
		}

		return Optional.of(proxy.getProxyStatus(client));
	}

	@GET
	@Path("/{id}")
	public Optional<Proxy> getProxy(@PathParam("id") long id) {
		return Optional.of(proxy_map.get(id));
	}

	@DELETE
	@Path("/{id}")
	public Optional<Proxy> delProxy(@PathParam("id") long id) {
		return Optional.of(proxy_map.remove(id));
	}

	@PUT
	public Optional<Proxy> addProxy(Proxy proxy) {
		return Optional.of(proxy_map.put(counter.incrementAndGet(), proxy));
	}

	@GET
	public Map<Long, Proxy> getProxies() {
		return proxy_map;
	}
}