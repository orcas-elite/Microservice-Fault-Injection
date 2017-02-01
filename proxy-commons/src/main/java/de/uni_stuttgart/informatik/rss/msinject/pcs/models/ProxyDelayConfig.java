package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

public class ProxyDelayConfig {
	private boolean enabled;
	private float probability;
	private int min;
	private int max;


	public ProxyDelayConfig() {
	}

	public ProxyDelayConfig(boolean enabled, float probability, int min, int max) {
		this.enabled = enabled;
		this.probability = probability;
		this.min = min;
		this.max = max;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public float getProbability() {
		return probability;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
