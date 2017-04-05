import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CommandRequest extends HystrixCommand<String> {

	private final HttpClient httpClient;
    private final String url;

    public CommandRequest(HttpClient httpClient, String url) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.httpClient = httpClient;
        this.url = url;
    }

    @Override
    protected String run() throws InterruptedException, ExecutionException, TimeoutException {
        return httpClient.GET(url).getContentAsString();
    }

    @Override
    protected String getFallback() {
        return "Failure";
    }
}