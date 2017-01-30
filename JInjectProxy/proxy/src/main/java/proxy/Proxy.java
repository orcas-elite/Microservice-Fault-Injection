package proxy;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.proxy.ProxyServlet.Transparent;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Proxy extends Transparent {
	private static final Logger logger = LoggerFactory.getLogger(Proxy.class);
	
	private static final long serialVersionUID = 1L;

	private boolean started = true;
	
	// Drop
	private boolean dropEnabled = false;
	private double dropProbability = 0.0;
	
	// Delay
	private boolean delayEnabled = false;
	private double delayProbability = 0.0;
	private int delayTimeMin = 0;
	private int delayTimeRandSpan = 0;
	
	// Max active, n-lane bridge
	private boolean maxActiveEnabled = false;
	private int maxActive = 2;
	private Object maxActiveLock = new Object();
	private static AtomicInteger activeRequestsCounter = new AtomicInteger(0);
	
	// Metrics
	private int requestsServiced = 0;
	private int requestsDelayed = 0;
	private int requestsDropped = 0;

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
		logger.info(String.format("Proxy setDrop %b %f", enable, probability));
		dropEnabled = enable;
		dropProbability = probability;
	}
	
	public void setDelay(boolean enable, double probability, int delayMin, int delayMax) {
		logger.info(String.format("Proxy setDelay %b %f %d %d", enable, probability, delayMin, delayMax));
		delayEnabled = enable;
		delayProbability = probability;
		delayTimeMin = delayMin;
		delayTimeRandSpan = delayMax - delayMin + 1;
	}
	

	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("Starting init proxy to " + config.getInitParameter("proxyTo"));
		super.init(config);
		logger.info("Proxy init finished");
		started = true;
	}

	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// Drop packages
		if (dropEnabled && rd.nextDouble() <= dropProbability) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			if(logger.isTraceEnabled())
				logger.trace("Dropped request " + request);
			requestsDropped++;
			return; // TODO Better drop
		}
		super.service(request, response);
		if(logger.isTraceEnabled())
			logger.trace("Serviced request " + request);
		requestsServiced++;
	}

	@Override
	protected void customizeProxyRequest(Request proxyRequest, HttpServletRequest request) {
		
		if (maxActiveEnabled) {
			while(activeRequestsCounter.intValue() >= maxActive) {
				try {
					synchronized (maxActiveLock) {
						maxActiveLock.wait();						
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
			activeRequestsCounter.incrementAndGet();
		}

		// Delay packages
		if (delayEnabled && rd.nextDouble() <= delayProbability) {
			try {
				int delayTime = delayTimeMin + rd.nextInt(delayTimeRandSpan);
				if (logger.isTraceEnabled())
					logger.trace("Delay request by " + delayTime + " " + request);
				Thread.sleep(delayTime);
				requestsDelayed++;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
		super.customizeProxyRequest(proxyRequest, request);
	}	

	
	
	// Observe ProxyResponseListener to know when complete
    protected Response.Listener newProxyResponseListener(HttpServletRequest request, HttpServletResponse response)
    {
        return new ProxyResponseListenerObserved(request, response);
    }
    
    protected class ProxyResponseListenerObserved extends ProxyResponseListener {

		protected ProxyResponseListenerObserved(HttpServletRequest request, HttpServletResponse response) {
			super(request, response);
		}
    	
		@Override
		public void onComplete(Result result) {
			super.onComplete(result);
			if (maxActiveEnabled) {
				activeRequestsCounter.decrementAndGet();
				synchronized (maxActiveLock) {
					maxActiveLock.notify();				
				}
			}
		}
    }
	

	public boolean isStarted() {
		return started;
	}
	
	public int getRequestsServiced() {
		return requestsServiced;
	}

	public int getRequestsDelayed() {
		return requestsDelayed;
	}

	public int getRequestsDropped() {
		return requestsDropped;
	}
}
