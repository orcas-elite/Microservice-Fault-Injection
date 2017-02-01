package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

public class ProxyStatus {
	private int controlPort;
	private int proxyPort;
	private String proxyTag;
	private String proxyUuid;
	private String proxyTarget;

	private boolean pcsConnected;
	private long requestsServiced = 0;
	private long requestsDelayed = 0;
	private long requestsDropped = 0;

	
	public ProxyStatus() {
	}

	public ProxyStatus(int controlPort, int proxyPort, String proxyTag, String proxyUuid, String proxyTarget,
			boolean pcsConnected, long requestsServiced, long requestsDelayed, long requestsDropped) {
		super();
		this.controlPort = controlPort;
		this.proxyPort = proxyPort;
		this.proxyTag = proxyTag;
		this.proxyUuid = proxyUuid;
		this.proxyTarget = proxyTarget;
		this.pcsConnected = pcsConnected;
		this.requestsServiced = requestsServiced;
		this.requestsDelayed = requestsDelayed;
		this.requestsDropped = requestsDropped;
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

	public boolean isPcsConnected() {
		return pcsConnected;
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
