package de.uni_stuttgart.informatik.rss.msinject.pcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class ProxyControlServerConfiguration extends Configuration {

	private int port;

	@JsonProperty
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}