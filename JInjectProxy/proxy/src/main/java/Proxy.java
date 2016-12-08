
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.proxy.ProxyServlet.Transparent;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Proxy extends Transparent {
	private static final long serialVersionUID = 1L;

	private boolean dropRequests = false;
	private boolean delayRequests = false;

	private final Random rd = new Random();
	private final Server proxyServer;
		
	
	public Proxy(Server proxyServer) {
		this.proxyServer = proxyServer;
	}

	public static Proxy startProxy(int proxyPort, String proxyTo) throws Exception {
		Server proxyServerTmp = new Server(proxyPort);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		proxyServerTmp.setHandler(context);
		Proxy proxy = new Proxy(proxyServerTmp);
		ServletHolder helloServletHolder = new ServletHolder(proxy);
		helloServletHolder.setInitParameter("proxyTo", proxyTo);
		helloServletHolder.setInitParameter("prefix", "/");
		context.addServlet(helloServletHolder, "/*");
		proxyServerTmp.start();
		return proxy;
	}

	public void joinProxy() throws InterruptedException {
		proxyServer.join();
	}

	
	

	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("proxyTo " + config.getInitParameter("proxyTo"));
		super.init(config);
		System.out.println("Proxy init done");
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		// System.out.println(">>> got a request !");

		// Drop test
		if (dropRequests && rd.nextBoolean())
			return;

		super.service(req, res);
	}

	@Override
	protected void customizeProxyRequest(Request proxyRequest, HttpServletRequest request) {
		if (delayRequests) {
			try {
				// System.out.println("Start wait");
				Thread.sleep(1000);
				// System.out.println("End wait");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.customizeProxyRequest(proxyRequest, request);
	}
}
