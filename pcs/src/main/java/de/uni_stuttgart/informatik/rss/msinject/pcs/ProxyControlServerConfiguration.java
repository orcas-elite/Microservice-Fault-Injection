package de.uni_stuttgart.informatik.rss.msinject.pcs;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

public class ProxyControlServerConfiguration extends Configuration {

	private int port;

	private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();


	public int getPort() {
		return port;
	}

	public JerseyClientConfiguration getJerseyClient() {
		return jerseyClient;
	}

}