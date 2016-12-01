package org.unistuttgart.msinject.proxy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
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

public class ProxyMain2 extends AbstractHandler {

	protected static final String ASYNC_CONTEXT = ProxyMain2.class.getName() + ".asyncContext";
	private static HttpClient httpClient = new HttpClient();
	
	private Set<HttpServletRequest> waitingRequests = new HashSet<HttpServletRequest>();

	private static final Set<String> HOP_HEADERS = new HashSet<String>();
	static {
		HOP_HEADERS.add("proxy-connection");
		HOP_HEADERS.add("connection");
		HOP_HEADERS.add("keep-alive");
		HOP_HEADERS.add("transfer-encoding");
		HOP_HEADERS.add("te");
		HOP_HEADERS.add("trailer");
		HOP_HEADERS.add("proxy-authorization");
		HOP_HEADERS.add("proxy-authenticate");
		HOP_HEADERS.add("upgrade");
	}


	public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		System.out.println("baseRequest " + baseRequest);
		System.out.println("request " + request);
		
		final Request proxyRequest = httpClient.newRequest("http://0.0.0.0:8080")
				.method(HttpMethod.fromString(request.getMethod()))
				.version(HttpVersion.fromString(request.getProtocol()));

		// Copy headers
		for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements();) {
			String headerName = headerNames.nextElement();
			String lowerHeaderName = headerName.toLowerCase(Locale.ENGLISH);

			// Remove hop-by-hop headers
			// if (HOP_HEADERS.contains(lowerHeaderName))
			// continue;
			//
			// if (_hostHeader != null && lowerHeaderName.equals("host"))
			// continue;

			for (Enumeration<String> headerValues = request.getHeaders(headerName); headerValues.hasMoreElements();) {
				String headerValue = headerValues.nextElement();
				if (headerValue != null)
					proxyRequest.header(headerName, headerValue);
			}
		}

		// proxyRequest.send(new ProxyResponseListener(request, response));

		System.out.println("Continuation");

		if (!waitingRequests.contains(request)) {
			waitingRequests.add(request);
			final Continuation continuation = ContinuationSupport.getContinuation(request);
			continuation.suspend();
			System.out.println("suspend");

			Thread thr = new Thread(new Runnable() {

				public void run() {

					System.out.println("Start sleep");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("End sleep");

					continuation.resume();
					System.out.println("resume");
				}
			});
			//thr.start();

			baseRequest.setHandled(true);
			System.out.println("Return");
			//return;
		}
		else {
		waitingRequests.remove(request);
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println("<h1>Hello Proxy</h1>");
		System.out.println("End");
		}
	}

	public static void main(String[] args) {
//
//		try {
//			httpClient.start();
//			System.out.println("HTTP client started");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		System.out.println("Starting Proxy");

		Server server = new Server(8081);
		server.setHandler(new ProxyMain2());

		try {
			server.start();
			System.out.println("Proxy Started");
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

//		try {
//			httpClient.stop();
//			System.out.println("HTTP client stopped");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private class ProxyResponseListener extends Response.Listener.Empty {
		private final HttpServletRequest request;
		private final HttpServletResponse response;

		public ProxyResponseListener(HttpServletRequest request, HttpServletResponse response) {
			this.request = request;
			this.response = response;
		}

		@Override
		public void onBegin(Response proxyResponse) {
			response.setStatus(proxyResponse.getStatus());
		}

		@Override
		public void onHeaders(Response proxyResponse) {
			onResponseHeaders(request, response, proxyResponse);

			// if (_log.isDebugEnabled())
			// {
			// StringBuilder builder = new StringBuilder("\r\n");
			// builder.append(request.getProtocol()).append("
			// ").append(response.getStatus()).append("
			// ").append(proxyResponse.getReason()).append("\r\n");
			// for (String headerName : response.getHeaderNames())
			// {
			// builder.append(headerName).append(": ");
			// for (Iterator<String> headerValues =
			// response.getHeaders(headerName).iterator();
			// headerValues.hasNext();)
			// {
			// String headerValue = headerValues.next();
			// if (headerValue != null)
			// builder.append(headerValue);
			// if (headerValues.hasNext())
			// builder.append(",");
			// }
			// builder.append("\r\n");
			// }
			// _log.debug("{} proxying to downstream:{}{}{}{}{}",
			// getRequestId(request),
			// System.lineSeparator(),
			// proxyResponse,
			// System.lineSeparator(),
			// proxyResponse.getHeaders().toString().trim(),
			// System.lineSeparator(),
			// builder);
			// }
		}

		protected void onResponseHeaders(HttpServletRequest request, HttpServletResponse response,
				Response proxyResponse) {
			for (HttpField field : proxyResponse.getHeaders()) {
				String headerName = field.getName();
				String lowerHeaderName = headerName.toLowerCase(Locale.ENGLISH);
				if (HOP_HEADERS.contains(lowerHeaderName))
					continue;

				String newHeaderValue = filterResponseHeader(request, headerName, field.getValue());
				if (newHeaderValue == null || newHeaderValue.trim().length() == 0)
					continue;

				response.addHeader(headerName, newHeaderValue);
			}
		}

		protected String filterResponseHeader(HttpServletRequest request, String headerName, String headerValue) {
			return headerValue;
		}

		protected void onResponseContent(HttpServletRequest request, HttpServletResponse response,
				Response proxyResponse, byte[] buffer, int offset, int length) throws IOException {
			response.getOutputStream().write(buffer, offset, length);
			// _log.debug("{} proxying content to downstream: {} bytes",
			// getRequestId(request), length);
		}

		protected void onResponseSuccess(HttpServletRequest request, HttpServletResponse response,
				Response proxyResponse) {
			AsyncContext asyncContext = (AsyncContext) request.getAttribute(ASYNC_CONTEXT);
			asyncContext.complete();
			// _log.debug("{} proxying successful", getRequestId(request));
		}

		protected void onResponseFailure(HttpServletRequest request, HttpServletResponse response,
				Response proxyResponse, Throwable failure) {
			// _log.debug(getRequestId(request) + " proxying failed", failure);
			if (!response.isCommitted()) {
				if (failure instanceof TimeoutException)
					response.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
				else
					response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
			}
			AsyncContext asyncContext = (AsyncContext) request.getAttribute(ASYNC_CONTEXT);
			asyncContext.complete();
		}

		@Override
		public void onContent(Response proxyResponse, ByteBuffer content) {
			byte[] buffer;
			int offset;
			int length = content.remaining();
			if (content.hasArray()) {
				buffer = content.array();
				offset = content.arrayOffset();
			} else {
				buffer = new byte[length];
				content.get(buffer);
				offset = 0;
			}

			try {
				onResponseContent(request, response, proxyResponse, buffer, offset, length);
			} catch (IOException x) {
				proxyResponse.abort(x);
			}
		}

		@Override
		public void onSuccess(Response proxyResponse) {
			onResponseSuccess(request, response, proxyResponse);
		}

		@Override
		public void onFailure(Response proxyResponse, Throwable failure) {
			onResponseFailure(request, response, proxyResponse, failure);
		}

		@Override
		public void onComplete(Result result) {
			// _log.debug("{} proxying complete", getRequestId(request));
			System.out.println("Complete");
		}
	}
}
