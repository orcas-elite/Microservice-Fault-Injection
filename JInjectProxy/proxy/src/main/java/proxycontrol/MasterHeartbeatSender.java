package proxycontrol;

import java.util.concurrent.ExecutionException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import proxy.Proxy;


/**
 * Sends hello message to master
 * 
 * @author Jonas Grunert
 *
 */
public class MasterHeartbeatSender {
	private static final Logger logger = LoggerFactory.getLogger(MasterHeartbeatSender.class);

	// Delay between send retries
	private static final int HeartbeatFrequency = 5000;
	private boolean pcsConnected = false;
	
	
	public MasterHeartbeatSender(Proxy proxy, String masterUrl) {

		Thread metricsThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Start http client
				HttpClient httpClient = null;
				try {
					httpClient = new HttpClient();
					httpClient.start();
				} catch (Exception e1) {
					logger.error("Failed to start http client", e1);
					return;
				}

				try {
					while (!Thread.interrupted()) {
						try {
							// Try to say hello to master
							Request request = httpClient.newRequest(masterUrl);
							request.method(HttpMethod.PUT);
					        request.content(new StringContentProvider(new Gson().toJson(proxy.getStatus())), "application/json");
							
					        ContentResponse response = request.send();
					        
							if(response.getStatus() == 200) {
								if(!proxy.isPcsConnected() && !pcsConnected) {
									pcsConnected = true;
									proxy.setPcsConnected(true);
									logger.info("Connected to PCS, Send heartbeat success: " + response.getStatus());
								}
								else {
									logger.trace("Send heartbeat success: " + response.getStatus());
								}
							}
							else {
								pcsConnected = false;
								logger.info("Send heartbeat failure code: " + response.getStatus());	
								proxy.setPcsConnected(false);							
							}
						} catch (ExecutionException e1) {
							pcsConnected = false;
							logger.warn("Failed to send hello message, unable to connect to " + masterUrl);
						} catch (Exception e1) {
							pcsConnected = false;
							logger.error("Failed to send hello message", e1);
						}
						
						// Retry delay
						try {
							Thread.sleep(HeartbeatFrequency);
						} catch (InterruptedException e) {
							logger.error("Interrupted", e);
							return;
						}
					}
				} finally {
					// Stop http client
					try {
						httpClient.stop();
					} catch (Exception e) {
						logger.error("Failed to stop http client", e);
					}
				}
			}
		});
		metricsThread.setName("MasterMessageSenderThread");
		metricsThread.setDaemon(true);
		metricsThread.start();
	}
}
