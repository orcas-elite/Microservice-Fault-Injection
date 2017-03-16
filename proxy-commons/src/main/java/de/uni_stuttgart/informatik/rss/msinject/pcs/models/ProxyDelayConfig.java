package de.uni_stuttgart.informatik.rss.msinject.pcs.models;

public class ProxyDelayConfig {
	/**
	 * Enables packet delay.
	 */	
	private boolean enabled;
	/**
	 * Probability of a packet to be delayed
	 */	
	private double probability;
	
	/**
	 * Type of distribution of delay times.
	 */	
	private DistributionType delayTimeDistribution;
	/**
	 * Delay time mean (in milliseconds).
	 */	
	private double delayTimeMean;
	/**
	 * Uniform distribution range or normal distribution standard deviation.
	 */
	private double delayTimeRangeSd;


	public ProxyDelayConfig() {
	}

	public ProxyDelayConfig(boolean enabled, float probability, 
			DistributionType distributionType, double delayTimeMean, double delayTimeRangeSd) {
		this.enabled = enabled;
		this.probability = probability;
		this.delayTimeDistribution = distributionType;
		this.delayTimeMean = delayTimeMean;
		this.delayTimeRangeSd = delayTimeRangeSd;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public double getProbability() {
		return probability;
	}

	public DistributionType getDelayTimeDistribution() {
		return delayTimeDistribution;
	}

	public double getDelayTimeMean() {
		return delayTimeMean;
	}

	public double getDelayTimeRangeSd() {
		return delayTimeRangeSd;
	}
}
