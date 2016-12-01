package org.unistuttgart.msinject.proxy;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.proxy.ProxyServlet;
//import org.eclipse.jetty.client.HttpClient;
//import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class ProxyMainJettyProxyServlet extends ProxyServlet { // TODO Transparent
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println(">> init done !");
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		System.out.println(">>> got a request !");
		
		final ServletRequest req2 = req;
		final ServletResponse res2 = res;
		
		// Not working
//		Thread t = new Thread(new Runnable() {
//			
//			public void run() {
//				// TODO Auto-generated method stub
//
//				try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				System.out.println("Sleeped");
//				
//				try {
//					ProxyMain.super.service(req2, res2);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});

		super.service(req2, res2);
	}

	@Override
	protected URI rewriteURI(HttpServletRequest request) {
//		URI rewrittenURI = super.rewriteURI(request);
//
//		String apiKeyName = this.getInitParameter("apiKeyName");
//
//		if (apiKeyName != null) {
//			String fragment = apiKeyName + "=" + this.getInitParameter("apiKeyValue");
//
//			String queryString = request.getQueryString();
//
//			rewrittenURI += (queryString == null ? "?" : "&") + fragment;
//
//			rewrittenURI = URI.create(rewrittenURI).normalize().toString();
//		}
//
//		return rewrittenURI;
				
		try {
			return new URI("http://0.0.0.0:8080/");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
//	public class ProxyServletHandler extends ServletHandler {
//		@Override
//		public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request,
//				HttpServletResponse response) throws IOException, ServletException {
//
//		}
//	}

	public static void main(String... args) throws Exception {

		Server server = new Server(8081);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(ProxyMainJettyProxyServlet.class, "/*");

		server.setHandler(servletHandler);
		server.start();
		server.join();
		// Server server = new Server(8080);
		//
		// ServletHandler servletHandler = new ServletHandler();
		// servletHandler.addServletWithMapping(MyProxyServlet.class, "/*");
		//
		// server.setHandler(servletHandler);
		// server.start();
		// server.join();
	}
}

// public class ProxyMain extends AbstractHandler {

// private static HttpClient httpClient = new HttpClient();
//
// public void handle(String target, Request baseRequest, HttpServletRequest
// request, HttpServletResponse response)
// throws IOException, ServletException {
//
// System.out.println("baseRequest " + baseRequest);
// System.out.println("request " + request);
//
// try {
// ContentResponse resp = httpClient.GET("http://0.0.0.0:8080/");
// System.out.println("ContentResponse " + resp);
//
// } catch (Exception e) {
// System.err.println("Proxy request failed");
// e.printStackTrace();
// }
//
// response.setContentType("text/html;charset=utf-8");
// response.setStatus(HttpServletResponse.SC_OK);
// baseRequest.setHandled(true);
// response.getWriter().println("<h1>Hello Proxy</h1>");
// }
//
// public static void main(String[] args) {
//
// try {
// httpClient.start();
// System.out.println("HTTP client started");
// } catch (Exception e) {
// e.printStackTrace();
// }
//
// System.out.println("Starting Proxy");
//
// Server server = new Server(8081);
// server.setHandler(new ProxyMain());
//
// try {
// server.start();
// System.out.println("Proxy Started");
// server.join();
// } catch (Exception e) {
// e.printStackTrace();
// }
//
// try {
// httpClient.stop();
// System.out.println("HTTP client stopped");
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
