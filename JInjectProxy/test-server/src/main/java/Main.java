
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Random;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Main extends AbstractHandler {
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
//		Random rd = new Random();
//		if(rd.nextBoolean())
//		{
//			System.out.println("return");
//			return;
//		}

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println("<h1>Hello Test" + request.getRequestURI() + "</h1>");
	}

	public static void main(String[] args) {
		System.out.println("Starting server");

		Server server = new Server(8080);
		server.setHandler(new Main());

		try {
			server.start();
			System.out.println("Started");
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}