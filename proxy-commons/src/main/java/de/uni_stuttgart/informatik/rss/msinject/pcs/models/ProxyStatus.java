package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

public class ProxyStatus {
	private int controlPort;
	private int proxyPort;
	private String proxyTag;
	private String proxyUuid;
	private String proxyTarget;

	private boolean started;
	private boolean pcsConnected;

	// Metrics
	private long requestsServiced;
	private long requestsDelayed;
	private long requestsDelayedTime;
	private long requestsNLaneDelayed;
	private long requestsNLaneDelayedTime;
	private long requestsDropped;
	// Duration of the actual request (excluding delays)
	private long requestsDuration;
	private long requestsContentLength;

	public ProxyStatus() {
	}

	public ProxyStatus(int controlPort, int proxyPort, String proxyTag, String proxyUuid, String proxyTarget,
			boolean started, boolean pcsConnected, long requestsServiced, long requestsDelayed,
			long requestsDelayedTime, long requestsNLaneDelayed, long requestsNLaneDelayedTime, long requestsDropped,
			long requestsDuration, long requestsContentLength) {
		super();
		this.controlPort = controlPort;
		this.proxyPort = proxyPort;
		this.proxyTag = proxyTag;
		this.proxyUuid = proxyUuid;
		this.proxyTarget = proxyTarget;
		this.started = started;
		this.pcsConnected = pcsConnected;
		this.requestsServiced = requestsServiced;
		this.requestsDelayed = requestsDelayed;
		this.requestsDelayedTime = requestsDelayedTime;
		this.requestsNLaneDelayed = requestsNLaneDelayed;
		this.requestsNLaneDelayedTime = requestsNLaneDelayedTime;
		this.requestsDropped = requestsDropped;
		this.requestsDuration = requestsDuration;
		this.requestsContentLength = requestsContentLength;
	}

	public int getControlPort() {
		return controlPort;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public String getProxyTag() {
		return proxyTag;
	}

	public String getProxyUuid() {
		return proxyUuid;
	}

	public String getProxyTarget() {
		return proxyTarget;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isPcsConnected() {
		return pcsConnected;
	}

	public long getRequestsServiced() {
		return requestsServiced;
	}

	public long getRequestsDelayed() {
		return requestsDelayed;
	}

	public long getRequestsDelayedTime() {
		return requestsDelayedTime;
	}

	public long getRequestsNLaneDelayed() {
		return requestsNLaneDelayed;
	}

	public long getRequestsNLaneDelayedTime() {
		return requestsNLaneDelayedTime;
	}

	public long getRequestsDropped() {
		return requestsDropped;
	}

	public long getRequestsDuration() {
		return requestsDuration;
	}

	public long getRequestsContentLength() {
		return requestsContentLength;
	}
}
