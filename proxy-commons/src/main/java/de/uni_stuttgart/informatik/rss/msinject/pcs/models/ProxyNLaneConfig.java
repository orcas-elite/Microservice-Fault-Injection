package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

public class ProxyNLaneConfig {
	private boolean enabled;
	private int maxActive;

	public ProxyNLaneConfig() {
	}

	public ProxyNLaneConfig(boolean enabled, int maxActive) {
		super();
		this.enabled = enabled;
		this.maxActive = maxActive;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getMaxActive() {
		return maxActive;
	}
}
