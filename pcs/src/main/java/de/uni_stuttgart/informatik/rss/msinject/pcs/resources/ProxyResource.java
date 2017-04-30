package de.uni_stuttgart.informatik.rss.msinject.pcs.resources;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import de.uni_stuttgart.informatik.rss.msinject.pcs.models.Proxy;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyDelayConfig;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyDropConfig;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyMetricsConfig;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyNLaneConfig;
import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyStatus;

@Path("/proxy")
@Produces(MediaType.APPLICATION_JSON)
public class ProxyResource {
    private final Cache<String, Proxy> proxy_map;
    private final Client client;
    private final Logger logger = LoggerFactory.getLogger("ProxyResource");


    public ProxyResource(Client client, long duration) {
        this.client = client;
        this.proxy_map = CacheBuilder.newBuilder().expireAfterWrite(duration, TimeUnit.SECONDS).build();
    }

    Stream<Proxy> getByTag(@Nonnull String tag) {
        return proxy_map.asMap().values().stream().filter(proxy -> proxy.getId().equals(tag));
    }

    Map<String, Set<String>> getTags() {
        return proxy_map.asMap().values().stream().collect(Collectors.groupingBy(Proxy::getId, Collectors.mapping(Proxy::getUuid, Collectors.toSet())));
    }

    @POST
    @Path("/{uuid}/delay")
    public Optional<Boolean> setDelayConfig(@PathParam("uuid") String uuid, ProxyDelayConfig delayConfig) {
        Proxy proxy = proxy_map.getIfPresent(uuid);

        if (proxy == null) {
            return Optional.absent();
        }

        return Optional.of(proxy.setDelayConfig(client, delayConfig));
    }

    @GET
    @Path("/{uuid}/delay")
    public Optional<ProxyDelayConfig> getDelayConfig(@PathParam("uuid") String uuid) {
        Proxy proxy = proxy_map.getIfPresent(uuid);

        if (proxy == null) {
            return Optional.absent();
        }

        return Optional.of(proxy.getDelayConfig(client));
    }

    @POST
    @Path("/{uuid}/drop")
    public Optional<Boolean> setDropConfig(@PathParam("uuid") String uuid, ProxyDropConfig dropConfig) {
        Proxy proxy = proxy_map.getIfPresent(uuid);

        if (proxy == null) {
            return Optional.absent();
        }

        return Optional.of(proxy.setDropConfig(client, dropConfig));
    }

    @GET
    @Path("/{uuid}/drop")
    public Optional<ProxyDropConfig> getDropConfig(@PathParam("uuid") String uuid) {
        Proxy proxy = proxy_map.getIfPresent(uuid);

        if (proxy == null) {
            return Optional.absent();
        }

        return Optional.of(proxy.getDropConfig(client));
    }

    @POST
    @Path("/{uuid}/nlane")
    public Optional<Boolean> setNLaneConfig(@PathParam("uuid") String uuid, ProxyNLaneConfig nLaneConfig) {
        Proxy proxy = proxy_map.getIfPresent(uuid);

        if (proxy == null) {
            return Optional.absent();
        }

        return Optional.of(proxy.setNLaneConfig(client, nLaneConfig));
    }

    @GET
    @Path("/{uuid}/nlane")
    public Optional<ProxyNLaneConfig> getNLaneConfig(@PathParam("uuid") String uuid) {
        Proxy proxy = proxy_map.getIfPresent(uuid);

        if (proxy == null) {
            return Optional.absent();
        }

        return Optional.of(proxy.getNLaneConfig(client));
    }

    @POST
    @Path("/{uuid}/metrics")
    public Optional<Boolean> setMetricsConfig(@PathParam("uuid") String uuid, ProxyMetricsConfig metricsConfig) {
        Proxy proxy = proxy_map.getIfPresent(uuid);

        if (proxy == null) {
            return Optional.absent();
        }

        return Optional.of(proxy.setMetricsConfig(client, metricsConfig));
    }

    @GET
    @Path("/{uuid}/metrics")
    public Optional<ProxyMetricsConfig> getMetricsConfig(@PathParam("uuid") String uuid) {
        Proxy proxy = proxy_map.getIfPresent(uuid);

        if (proxy == null) {
            return Optional.absent();
        }

        return Optional.of(proxy.getMetricsConfig(client));
    }

    @GET
    @Path("/{uuid}/status")
    public Optional<ProxyStatus> getProxyStatus(@PathParam("uuid") String uuid) {
        Proxy proxy = proxy_map.getIfPresent(uuid);

        if (proxy == null) {
            return Optional.absent();
        }

        return Optional.of(proxy.getProxyStatus(client));
    }

    @GET
    @Path("/{uuid}")
    public Optional<Proxy> getProxy(@PathParam("uuid") String uuid) {
        return Optional.fromNullable(proxy_map.getIfPresent(uuid));
    }

    @DELETE
    @Path("/{uuid}")
    public Optional<Proxy> delProxy(@PathParam("uuid") String uuid) {
        Proxy old = proxy_map.getIfPresent(uuid);
        proxy_map.invalidate(uuid);
        return Optional.fromNullable(old);
    }

    @PUT
    public Optional<Proxy> addProxy(@Context HttpServletRequest req, ProxyStatus proxy) {
        final Proxy p = new Proxy();
        p.setAddress(req.getRemoteAddr());
        p.setControlPort(proxy.getControlPort());
        p.setProxyPort(proxy.getProxyPort());
        p.setUuid(proxy.getProxyUuid());
        p.setId(proxy.getProxyTag());
        proxy_map.put(proxy.getProxyUuid(), p);
        return Optional.of(p);
    }

    @GET
    public Map<String, Proxy> getProxies() {
        return proxy_map.asMap();
    }

}
