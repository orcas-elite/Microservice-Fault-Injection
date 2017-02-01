package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

public class ProxyStatus {
	public short controlPort;
	public short proxyPort;
	public String proxyTag;
	public String proxyUuid;

	public long requestsServiced = 0;
	public long requestsDelayed = 0;
	public long requestsDropped = 0;

	
	public ProxyStatus() {
	}

	public ProxyStatus(short controlPort, short proxyPort, String proxyTag, String proxyUuid, long requestsServiced,
			long requestsDelayed, long requestsDropped) {
		super();
		this.controlPort = controlPort;
		this.proxyPort = proxyPort;
		this.proxyTag = proxyTag;
		this.proxyUuid = proxyUuid;
		this.requestsServiced = requestsServiced;
		this.requestsDelayed = requestsDelayed;
		this.requestsDropped = requestsDropped;
	}	
}
