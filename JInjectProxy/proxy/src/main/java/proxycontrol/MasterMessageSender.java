package proxycontrol;

import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.jetty.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;


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
	
	
	public MasterMessageSender(String masterUrl, String proxyId, String proxyUuid) {

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
							
							JsonHelloMsg helloJson = new JsonHelloMsg(proxyId, proxyUuid);							
							String helloMsg = new Gson().toJson(helloJson);
							out.write(helloMsg);
							out.close();
							
							if(httpCon.getResponseCode() == 200) {
								logger.info("Send hello success: " + httpCon.getResponseCode());
								return;
							}
							else {
								logger.info("Send hello failure code: " + httpCon.getResponseCode());								
							}
						} catch (ConnectException e1) {
							logger.info("Failed to send hello message, unable to connect to " + masterUrl);
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
	

	@SuppressWarnings("unused")
	private class JsonHelloMsg {
		public String proxyId;
		public String proxyUuid;

		public JsonHelloMsg(String proxyId, String proxyUuid) {
			super();
			this.proxyId = proxyId;
			this.proxyUuid = proxyUuid;
		}
	}
}
