package proxycontrol;

import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.jetty.client.HttpClient;
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
	private static final int RetryDelay = 4000;
	
	
	public MasterMessageSender(String masterUrl) {

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
							URL url = new URL("http://0.0.0.0:8080/");
							HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
							httpCon.setDoOutput(true);
							httpCon.setRequestMethod("PUT");
							OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
							out.write("Resource content");
							out.close();
							httpCon.getInputStream();
							
							logger.info("Send hello success");
							return;
						} catch (ConnectException e1) {
							logger.info("Failed to send hello message, unable to connect");
						} catch (Exception e1) {
							logger.error("Failed to send hello message", e1);
						}
						
						// Retry delay
						try {
							Thread.sleep(RetryDelay);
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
