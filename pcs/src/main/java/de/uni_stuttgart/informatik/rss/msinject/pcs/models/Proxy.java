package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class Proxy {
	private String address;
	private short control_port;
	private short proxy_port;

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

	public Boolean setDelayConfig(Client client, ProxyDelayConfig delayConfig) {

		Response response = client.target(address + ":" + control_port + "/control/set/delay").request().post(Entity.json(delayConfig));

		return response.getStatus() == 200;
	}

	public Boolean setDropConfig(Client client, ProxyDropConfig dropConfig) {

		Response response = client.target(address + ":" + control_port + "/control/set/drop").request().post(Entity.json(dropConfig));

		return response.getStatus() == 200;
	}

	public ProxyStatus getProxyStatus(Client client) {

		return client.target(address + ":" + control_port + "/control/status").request().get(ProxyStatus.class);

	}
}
