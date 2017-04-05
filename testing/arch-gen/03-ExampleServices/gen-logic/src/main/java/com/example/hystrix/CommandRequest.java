package com.example.hystrix;

import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;


public class CommandRequest extends HystrixCommand<String> {

	private final RestTemplate restTemplate;
    private final String requestUrl;

    public CommandRequest(RestTemplate restTemplate, String groupName, String requestUrl) {
        super(HystrixCommandGroupKey.Factory.asKey(groupName));
        this.restTemplate = restTemplate;
        this.requestUrl = requestUrl;
    }

    @Override
    protected String run() {
		restTemplate.getForObject(requestUrl, String.class);
		return "success";
    }
}