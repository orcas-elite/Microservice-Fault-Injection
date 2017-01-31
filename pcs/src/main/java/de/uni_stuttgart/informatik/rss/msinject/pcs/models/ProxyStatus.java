package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProxyStatus {

	@JsonProperty("proxy")
	public String status;
	public long requestsServiced = 0;
	public long requestsDelayed = 0;
	public long requestsDropped = 0;

	public ProxyStatus() {
	}

	public String getStatus() {
		return status;
	}

	public long getRequestsServiced() {
		return requestsServiced;
	}

	public long getRequestsDelayed() {
		return requestsDelayed;
	}

	public long getRequestsDropped() {
		return requestsDropped;
	}
}
