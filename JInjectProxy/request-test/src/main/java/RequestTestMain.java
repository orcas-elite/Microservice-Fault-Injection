
import org.eclipse.jetty.client.HttpClient;

public class RequestTestMain {

	public static void main(String[] args) throws Exception {
		String requestUrl = "http://0.0.0.0:8081/";
		int testRuns = 10000;

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
}