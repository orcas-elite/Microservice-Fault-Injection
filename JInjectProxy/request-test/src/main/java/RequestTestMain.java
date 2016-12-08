
import org.eclipse.jetty.client.HttpClient;

public class RequestTestMain {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			showHelp();
			return;
		}
		
		String requestUrl = args[0];
		int testRuns = 1000;

		HttpClient httpClient = new HttpClient();
		httpClient.start();

		// Warm up
		httpClient.GET(requestUrl);
		
		long waitTimeSum = 0;
		for (int i = 0; i < testRuns; i++) {
			String url = requestUrl + i;
			long start = System.nanoTime();
			httpClient.GET(url);
			waitTimeSum += (System.nanoTime() - start);
		}
		waitTimeSum /= testRuns;

		System.out.println("Average wait time on " + requestUrl + " over " + testRuns + " runs: ");
		System.out.println(((double) (waitTimeSum / 1000) / 1000) + " ms");

		httpClient.stop();
	}

	private static void showHelp() {
		System.out.println("Usage: [request-target]");
		System.out.println("Example: http://0.0.0.0:8081/");
	}
}