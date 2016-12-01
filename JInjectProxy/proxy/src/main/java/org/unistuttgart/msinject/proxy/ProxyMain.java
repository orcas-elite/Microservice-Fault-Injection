package org.unistuttgart.msinject.proxy;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.proxy.ProxyServlet.Transparent;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.Random;

public class ProxyMain extends Transparent {
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("proxyTo " + config.getInitParameter("proxyTo"));
		super.init(config);
		System.out.println("Proxy init done");
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		//System.out.println(">>> got a request !");
		
		// Drop test
//		Random rd = new Random();
//		if(rd.nextBoolean())
//			return;

		super.service(req, res);
	}

	@Override
	protected void customizeProxyRequest(Request proxyRequest, HttpServletRequest request) {
		try {
			//System.out.println("Start wait");
			Thread.sleep(1000);
			//System.out.println("End wait");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.customizeProxyRequest(proxyRequest, request);
	}


	public static void main(String... args) throws Exception {  
		Server server = new Server(8081);
				
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

		
		ServletHolder helloServletHolder = new ServletHolder(new ProxyMain());
		helloServletHolder.setInitParameter("proxyTo", "http://0.0.0.0:8080/");
		helloServletHolder.setInitParameter("prefix", "/");
		context.addServlet(helloServletHolder,"/*");
		
		server.start();
		server.join();
	}
}
