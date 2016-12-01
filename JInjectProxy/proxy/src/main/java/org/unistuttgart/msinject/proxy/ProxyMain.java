package org.unistuttgart.msinject.proxy;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

public class ProxyMain extends ProxyServlet { // TODO Transparent
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println(">> init done !");
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		System.out.println(">>> got a request !");
		
		Random rd = new Random();
		if(rd.nextBoolean())
			return;

		super.service(req, res);
	}

	@Override
	protected void customizeProxyRequest(Request proxyRequest, HttpServletRequest request) {
		try {
			System.out.println("Start wait");
			Thread.sleep(5000);
			System.out.println("End wait");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.customizeProxyRequest(proxyRequest, request);
	}

	@Override
	protected URI rewriteURI(HttpServletRequest request) {
		// URI rewrittenURI = super.rewriteURI(request);
		//
		// String apiKeyName = this.getInitParameter("apiKeyName");
		//
		// if (apiKeyName != null) {
		// String fragment = apiKeyName + "=" +
		// this.getInitParameter("apiKeyValue");
		//
		// String queryString = request.getQueryString();
		//
		// rewrittenURI += (queryString == null ? "?" : "&") + fragment;
		//
		// rewrittenURI = URI.create(rewrittenURI).normalize().toString();
		// }
		//
		// return rewrittenURI;

		try {
			return new URI("http://0.0.0.0:8080/");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void main(String... args) throws Exception {

		Server server = new Server(8081);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(ProxyMain.class, "/*");

		server.setHandler(servletHandler);
		server.start();
		server.join();
	}
}
