package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

public class ProxyMetricsConfig {
	private boolean enabled;

	public ProxyMetricsConfig() {
	}

	public ProxyMetricsConfig(boolean enabled) {
		super();
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
