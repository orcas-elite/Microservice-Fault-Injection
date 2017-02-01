package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class Proxy {
	private String address;
	private short controlPort;
	private short proxyPort;
	private String id;
	private String uuid;

	public Proxy() {
	}

	public String getAddress() {
		return address;
	}

	public short getControlPort() {
		return controlPort;
	}

	public short getProxyPort() {
		return proxyPort;
	}

	public String getId() {
		return id;
	}

	public String getUuid() {
		return uuid;
	}

	public Boolean setDelayConfig(Client client, ProxyDelayConfig delayConfig) {
		Response response = client.target("http://" + address + ":" + controlPort + "/control/delay").request().put(Entity.json(delayConfig));
		return response.getStatus() == 200;
	}

	public ProxyDelayConfig getDelayConfig(Client client) {
		return client.target("http://" + address + ":" + controlPort + "/control/delay").request().get(ProxyDelayConfig.class);
	}

	public Boolean setDropConfig(Client client, ProxyDropConfig dropConfig) {
		Response response = client.target("http://" + address + ":" + controlPort + "/control/drop").request().put(Entity.json(dropConfig));
		return response.getStatus() == 200;
	}

	public ProxyDropConfig getDropConfig(Client client) {
		return client.target("http://" + address + ":" + controlPort + "/control/drop").request().get(ProxyDropConfig.class);
	}

	public Boolean setNLaneConfig(Client client, ProxyNLaneConfig nLaneConfig) {
		Response response = client.target("http://" + address + ":" + controlPort + "/control/nlane").request().put(Entity.json(nLaneConfig));
		return response.getStatus() == 200;
	}

	public ProxyNLaneConfig getNLaneConfig(Client client) {
		return client.target("http://" + address + ":" + controlPort + "/control/nlane").request().get(ProxyNLaneConfig.class);
	}

	public Boolean setMetricsConfig(Client client, ProxyMetricsConfig metricConfig) {
		Response response = client.target("http://" + address + ":" + controlPort + "/control/metrics").request().put(Entity.json(metricConfig));
		return response.getStatus() == 200;
	}

	public ProxyMetricsConfig getMetricsConfig(Client client) {
		return client.target("http://" + address + ":" + controlPort + "/control/nlane").request().get(ProxyMetricsConfig.class);
	}

	public ProxyStatus getProxyStatus(Client client) {
		return client.target("http://" + address + ":" + controlPort + "/control/status").request().get(ProxyStatus.class);
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
