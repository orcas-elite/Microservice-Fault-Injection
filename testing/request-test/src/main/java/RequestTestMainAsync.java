
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.jetty.client.HttpClient;

public class RequestTestMainAsync {
	
	static AtomicLong waitTimeSum; 
	static AtomicInteger responses;
	static AtomicInteger runningRequests;

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			showHelp();
			return;
		}
		
		String requestUrl = args[0];
		int testRuns = 10;

		final HttpClient httpClient = new HttpClient();
		httpClient.start();

		// Warm up
		httpClient.GET(requestUrl);
		
		waitTimeSum = new AtomicLong(0);
		responses = new AtomicInteger(0);
		runningRequests = new AtomicInteger(0);
		for (int i = 0; i < testRuns; i++) {
			final String url = requestUrl + i;
			new Thread(new Runnable() {				
				public void run() {
//					System.out.println("req");
					long start = System.nanoTime();
					runningRequests.incrementAndGet();
					try {
						httpClient.GET(url);
					} catch (Exception e) {
						e.printStackTrace();
					}
					waitTimeSum.addAndGet(System.nanoTime() - start);
					responses.incrementAndGet();
					runningRequests.decrementAndGet();
//					System.out.println("resp");
				}
			}).start();
		}

		// Wait for completion
		while(responses.intValue() < testRuns) {
			System.out.println(runningRequests.intValue() + " active");
			Thread.sleep(500);
		}
		
		long avgWaitTime = waitTimeSum.longValue() / testRuns;
		
		System.out.println("Average wait time on " + requestUrl + " over " + testRuns + " runs: ");
		System.out.println(((double) (avgWaitTime / 1000) / 1000) + " ms");

		httpClient.stop();
	}

	private static void showHelp() {
		System.out.println("Usage: [request-target]");
		System.out.println("Example: http://0.0.0.0:8081/");
	}
}