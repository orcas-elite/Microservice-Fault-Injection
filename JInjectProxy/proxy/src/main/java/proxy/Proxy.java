package proxy;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Proxy extends Transparent {
	private static final Logger logger = LoggerFactory.getLogger(Proxy.class);
	
	private static final long serialVersionUID = 1L;

	public boolean started = true;
	
	public boolean dropEnabled = false;
	public double dropProbability = 0.0;
	
	public boolean delayEnabled = false;
	public double delayProbability = 0.0;
	public int delayTimeMin = 0;
	public int delayTimeRandSpan = 0;

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

	
	public void setDrop(boolean enable, double probability) {
		dropEnabled = enable;
		dropProbability = probability;
	}
	
	public void setDelay(boolean enable, double probability, int delayMin, int delayMax) {
		delayEnabled = enable;
		delayProbability = probability;
		delayTimeMin = delayMin;
		delayTimeRandSpan = delayMax - delayMin + 1;
	}
	

	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("Starting init proxy to " + config.getInitParameter("proxyTo"));
		super.init(config);
		logger.info("Proxy init done");
		started = true;
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		// Drop packages
		if (dropEnabled && rd.nextDouble() <= dropProbability)
			return;  // TODO Better drop

		super.service(req, res);
	}

	@Override
	protected void customizeProxyRequest(Request proxyRequest, HttpServletRequest request) {
		// Delay packages
		if (delayEnabled && rd.nextDouble() <= delayProbability) {
			try {
				Thread.sleep(delayTimeMin + rd.nextInt(delayTimeRandSpan));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.customizeProxyRequest(proxyRequest, request);
	}
}
