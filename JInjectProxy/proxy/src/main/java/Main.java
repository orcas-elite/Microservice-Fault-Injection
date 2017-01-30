
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proxy.Proxy;
import proxycontrol.ProxyControl;


public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private static InfluxDB influxDB;

	public static void main(String... args) throws Exception {
		if (args.length < 6) {
			showHelp();
			return;
		}

		Proxy proxy = null;
		Server proxyControlServer = null;
		try {
			int controlPort = Integer.parseInt(args[0]);
			int proxyPort = Integer.parseInt(args[1]);
			String proxyTo = args[2];
			String proxyId = args[3];
			String masterUrl = args[4];
			String influxdbUrl = args[5];
			
			// Try to connect to influxdb
			connectToDatabase(influxdbUrl);

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
			logger.error("MAIN exception", e);
			showHelp();
		} finally {
			if (proxyControlServer != null)
				proxyControlServer.destroy();
			if (proxy != null)
				proxy.destroy();
		}
	}
	
	private static void connectToDatabase(String influxdbUrl) {
		try {
			// Connect to influxdb database
			logger.info("Start connecting to InfluxDB");
			InfluxDB influxDB = InfluxDBFactory.connect(influxdbUrl, "root", "root");
			String dbName = "ProxyMetrics";
			influxDB.createDatabase(dbName);
			logger.info("Connected to InfluxDB");
		} catch (Exception e) {
			logger.error("connectToDatabase exception", e);
		}
	}

	private static void showHelp() {
		System.out.println("Usage: [control-port] [proxy-listen-port] [proxyTo] [proxy-id] [master-url] [influxdb-url]");
		System.out.println("Example: 8088 8081 http://0.0.0.0:8080/ http://0.0.0.0:8089/ ProxyForDatabase  http://0.0.0.0:8080/ http://172.17.0.2:8086/");
	}
}
