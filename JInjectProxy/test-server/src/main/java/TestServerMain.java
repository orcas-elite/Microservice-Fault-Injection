
import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationListener;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;


public class TestServerMain extends AbstractHandler {
	
	private static int ResponseDelay = 0;
	
	
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (ResponseDelay > 0) {
			Continuation continuation = ContinuationSupport.getContinuation(request);

			if (continuation.isInitial()) {
				continuation.setTimeout(ResponseDelay);
				continuation.suspend();
			} else {
				sendResponse(baseRequest, request, response);
			}

			continuation.addContinuationListener(new ContinuationListener() {
				public void onTimeout(Continuation continuation) {
					continuation.resume();
				}

				public void onComplete(Continuation continuation) {
				}
			});
		} else {
			System.out.println("x " + request);
			sendResponse(baseRequest, request, response);
		}
	}

	private void sendResponse(Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
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
		if(args.length >= 2) {
			ResponseDelay = Integer.parseInt(args[1]);
		}
		
		System.out.println("Starting server. Delay: " + ResponseDelay);

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