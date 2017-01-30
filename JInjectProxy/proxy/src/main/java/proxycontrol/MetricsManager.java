package proxycontrol;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proxy.Proxy;


/**
 * Monitors proxy metrics and loggs them to database, if proxy metrics enabled.
 * 
 * @author Jonas Grunert
 *
 */
public class MetricsManager {
	private static final Logger logger = LoggerFactory.getLogger(MetricsManager.class);
	
	// Frequency pushing metrics to time series database
	private static final int DbPushFrequency = 1000;
	
	private long requestsServiced = 0;
	private long requestsDelayed = 0;
	private long requestsDropped = 0;
	
	
	public MetricsManager(String proxyId, String proxyUuid, InfluxDB influxDB, Proxy proxy, 
			String dbName) {
		Thread metricsThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.interrupted()) {
					try {
						Thread.sleep(DbPushFrequency);
						
						if(proxy.getMetricsEnabled()) {
							long proxyRequestsServiced = proxy.getRequestsServiced();
							long proxyRequestsDelayed = proxy.getRequestsDelayed();
							long proxyRequestsDropped = proxy.getRequestsDropped();
							
							long requestsServicedDelta = proxyRequestsServiced - requestsServiced;
							long requestsDelayedDelta = proxyRequestsDelayed - requestsDelayed;
							long requestsDroppedDelta = proxyRequestsDropped - requestsDropped;
							
							Point point = Point.measurement("ProcessedRequests")
					                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
									.tag("proxyId", proxyId)
									.tag("proxyUuid", proxyUuid)
									.addField("requestsServiced", requestsServicedDelta)
									.addField("requestsDelayed", requestsDelayedDelta)
									.addField("requestsDropped", requestsDroppedDelta)
									.build();							
							influxDB.write(dbName, "autogen", point);
														
							requestsServiced = proxyRequestsServiced;
							requestsDelayed = proxyRequestsDelayed;
							requestsDropped = proxyRequestsDropped;
						}
					} catch (InterruptedException e) {
						logger.error("Interrupted", e);
						return;
					}
				}
			}
		});
		metricsThread.setName("MetricsThread");
		metricsThread.setDaemon(true);
		metricsThread.start();
	}
}
