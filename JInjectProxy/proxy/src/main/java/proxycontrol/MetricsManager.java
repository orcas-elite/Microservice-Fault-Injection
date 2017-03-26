package proxycontrol;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyStatus;
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
	
	
	public MetricsManager(String proxyTag, String proxyUuid, InfluxDB influxDB, Proxy proxy, 
			String dbName) {
		Thread metricsThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.interrupted()) {
					try {
						Thread.sleep(DbPushFrequency);
						
						if(proxy.getMetricsEnabled()) {
							proxy.getRequestsServiced();
							
							ProxyStatus proxyStatus = proxy.getStatus();

							Point point = Point.measurement("ProcessedRequests")
					                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
									.tag("proxyUuid", proxyUuid)
									.tag("proxyTag", proxyTag)
									.addField("requestsServiced", proxyStatus.getRequestsServiced())
									.addField("requestsDelayed", proxyStatus.getRequestsDelayed())
									.addField("requestsDelayedTime", proxyStatus.getRequestsDelayedTime())
									.addField("requestsNLaneDelayed", proxyStatus.getRequestsNLaneDelayed())
									.addField("requestsNLaneDelayedTime", proxyStatus.getRequestsNLaneDelayedTime())
									.addField("requestsDropped", proxyStatus.getRequestsDropped())
									.addField("requestsDuration", proxyStatus.getRequestsDuration())
									.addField("requestsContentLength", proxyStatus.getRequestsContentLength())
									.build();							
							influxDB.write(dbName, "autogen", point);
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
