package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProxyStatus {

	@JsonProperty("proxy")
	private String status;

	public ProxyStatus() {
	}

	public ProxyStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
