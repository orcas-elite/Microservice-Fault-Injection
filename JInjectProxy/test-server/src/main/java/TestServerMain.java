
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;


public class TestServerMain extends AbstractHandler {
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {		
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println("<h1>Hello Test" + request.getRequestURI() + "</h1>");
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			showHelp();
			return;
		}
		
		System.out.println("Starting server");

		Server server = new Server(Integer.parseInt(args[0]));
		server.setHandler(new TestServerMain());

		try {
			server.start();
			System.out.println("Started TestServer");
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private static void showHelp() {
		System.out.println("Usage: [server-port]");
	}
}