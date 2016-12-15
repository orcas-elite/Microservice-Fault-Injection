package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

public class ProxyDropConfig {
	private boolean enabled;
	private float probability;

	public ProxyDropConfig() {
	}

	public ProxyDropConfig(boolean enabled, float probability) {
		this.enabled = enabled;
		this.probability = probability;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public float getProbability() {
		return probability;
	}
}
