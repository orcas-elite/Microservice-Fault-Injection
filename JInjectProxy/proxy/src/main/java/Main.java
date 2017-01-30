
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import proxy.Proxy;
import proxycontrol.ProxyControl;


public class Main {

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
		System.out.println("Usage: [control-port] [proxy-listen-port] [proxyTo] [proxy-id] [master-url] [influxdb-url]");
		System.out.println("Example: 8088 8081 http://0.0.0.0:8080/ http://0.0.0.0:8089/ ProxyForDatabase  http://0.0.0.0:8080/ http://172.17.0.2:8086/");
	}
}
