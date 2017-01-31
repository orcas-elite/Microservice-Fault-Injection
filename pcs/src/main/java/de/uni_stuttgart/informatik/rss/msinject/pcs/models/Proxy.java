package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class Proxy {
	private String address;
	private short control_port;
	private short proxy_port;
	private String id;
	private String uuid;

	public Proxy() {
	}

	public String getAddress() {
		return address;
	}

	public short getControl_port() {
		return control_port;
	}

	public short getProxy_port() {
		return proxy_port;
	}

	public String getId() {
		return id;
	}

	public String getUuid() {
		return uuid;
	}

	public Boolean setDelayConfig(Client client, ProxyDelayConfig delayConfig) {

		Response response = client.target(address + ":" + control_port + "/control/set/delay").request().put(Entity.json(delayConfig));

		return response.getStatus() == 200;
	}

	public Boolean setDropConfig(Client client, ProxyDropConfig dropConfig) {

		Response response = client.target(address + ":" + control_port + "/control/set/drop").request().put(Entity.json(dropConfig));

		return response.getStatus() == 200;
	}

	public Boolean setNLaneConfig(Client client, ProxyNLaneConfig nLaneConfig) {

		Response response = client.target(address + ":" + control_port + "/control/set/nlane").request().put(Entity.json(nLaneConfig));

		return response.getStatus() == 200;
	}

	public Boolean setMetricsConfig(Client client, ProxyMetricsConfig metricConfig) {

		Response response = client.target(address + ":" + control_port + "/control/set/metrics").request().put(Entity.json(metricConfig));

		return response.getStatus() == 200;
	}

	public ProxyStatus getProxyStatus(Client client) {

		return client.target(address + ":" + control_port + "/control/status").request().get(ProxyStatus.class);

	}

	public void setAddress(String address) {
		this.address = address;
	}
}
