
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
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import proxy.Proxy;
import proxycontrol.ProxyControl;

public class Main extends Transparent {
	private static final long serialVersionUID = 1L;

	private boolean dropRequests = false;
	private boolean delayRequests = false;

	private Random rd = new Random();

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

	public static void main(String... args) throws Exception {
		if (args.length < 3) {
			showHelp();
			return;
		}

		Proxy proxy = null;
		Server proxyControlServer = null;
		try {
			int controlPort = Integer.parseInt(args[0]);
			int proxyPort = Integer.parseInt(args[1]);
			String proxyTo = args[2];

			// Start proxy
			proxy = Proxy.startProxy(proxyPort, proxyTo);
			
			// Start control server
			ResourceConfig config = new ResourceConfig();
			config.packages("proxycontrol");
			ServletHolder servlet = new ServletHolder(new ServletContainer(config));
			proxyControlServer = new Server(controlPort);
			ServletContextHandler context = new ServletContextHandler(proxyControlServer, "/*");
			context.addServlet(servlet, "/*");
			proxyControlServer.start();
			ProxyControl.proxy = proxy;

			proxy.joinProxy();
			//proxyControlServer.join();
		} catch (Exception e) {
			e.printStackTrace();
			showHelp();
		} finally {
			if (proxyControlServer != null)
				proxyControlServer.destroy();
			if (proxy != null)
				proxy.destroy();
		}
	}

	private static void showHelp() {
		System.out.println("Usage: [control-port] [proxy-listen-port] [proxyTo]");
		System.out.println("Example: 8088 8081 http://0.0.0.0:8080/");
	}
}
