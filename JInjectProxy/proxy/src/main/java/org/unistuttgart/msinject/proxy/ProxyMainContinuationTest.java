package org.unistuttgart.msinject.proxy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;


public class ProxyMainContinuationTest extends HttpServlet {

	public void init(ServletConfig servletConfig) throws ServletException {
		System.out.println(servletConfig.getServletContext().getInitParameter("commandPrefix"));
		super.init(servletConfig);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws java.io.IOException {

		String reqId = req.getParameter("id");

		Continuation cc = ContinuationSupport.getContinuation(req);

		res.setContentType("text/plain");
		res.getWriter().println("Request: " + reqId + "\tstart:\t" + new Date());
		res.getWriter().flush();

		cc.setTimeout(2000);
		cc.suspend();

		res.getWriter().println("Request: " + reqId + "\tend:\t" + new Date());
		if (cc.isInitial() != true) {
			cc.complete();
		}
		//throw new RuntimeException();
	}

	public static void main(String[] args) throws Exception {

		Server server = new Server(8081);

		// ServletContextHandler context2 = new
		// ServletContextHandler(ServletContextHandler.SESSIONS);
		// context2.setContextPath("/cgi");
		// context2.setResourceBase("./cgi-bin");
		// context2.setInitParameter("commandPrefix", "perl");
		// context2.addServlet(new ServletHolder(new Cgi()), "/");
		// server.setHandler(context2);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(ProxyMainContinuationTest.class, "/*");

		server.setHandler(servletHandler);
		server.start();
		server.join();
	}
}
