package de.uni_stuttgart.informatik.rss.msinject.pcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ProxyControlServerConfiguration extends Configuration {

	private int port;

	@Valid
	@NotNull
	private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();


	@JsonProperty
	public int getPort() {
		return port;
	}

	@JsonProperty
	public JerseyClientConfiguration getJerseyClientConfiguration() {
		return jerseyClient;
	}

}