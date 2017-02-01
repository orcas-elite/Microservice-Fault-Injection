package proxycontrol;

import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.jetty.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.uni_stuttgart.informatik.rss.msinject.pcs.models.ProxyStatus;
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
							URL url = new URL(masterUrl);
							HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
							httpCon.setDoOutput(true);
							httpCon.setRequestMethod("PUT");
							OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
							
							ProxyStatus statusMsg = proxy.getStatus();		
							out.write(new Gson().toJson(statusMsg));
							out.close();
							
							if(httpCon.getResponseCode() == 200) {
								if(!proxy.isPcsConnected()) {
									proxy.setPcsConnected(true);
									logger.trace("Connected to PCS, Send heartbeat success: " + httpCon.getResponseCode());
								}
								else {
									logger.trace("Send heartbeat success: " + httpCon.getResponseCode());
								}
								return;
							}
							else {
								logger.info("Send heartbeat failure code: " + httpCon.getResponseCode());	
								proxy.setPcsConnected(false);							
							}
						} catch (ConnectException e1) {
							logger.info("Failed to send hello message, unable to connect to " + masterUrl);
						} catch (Exception e1) {
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
