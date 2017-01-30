package proxycontrol;

import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sends hello message to master
 * 
 * @author Jonas Grunert
 *
 */
public class MasterMessageSender {
	private static final Logger logger = LoggerFactory.getLogger(MasterMessageSender.class);
	
	// Delay between send retries
	private static final int RetryDelay = 1000;
	
	
	public MasterMessageSender() {

		Thread metricsThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.interrupted()) {
					try {
						Thread.sleep(RetryDelay);
						
					} catch (InterruptedException e) {
						logger.error("Interrupted", e);
						return;
					}
				}
			}
		});
		metricsThread.setName("MasterMessageSenderThread");
		metricsThread.setDaemon(true);
		metricsThread.start();
	}
}
