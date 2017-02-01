package de.uni_stuttgart.informatik.rss.msinject.pcs;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

public class ProxyControlServerConfiguration extends Configuration {

	private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();
	private long timeoutDuration = 5;


	public JerseyClientConfiguration getJerseyClient() {
		return jerseyClient;
	}

	public long getTimeoutDuration() {
		return timeoutDuration;
	}

	public void setTimeoutDuration(long timeoutDuration) {
		this.timeoutDuration = timeoutDuration;
	}
}