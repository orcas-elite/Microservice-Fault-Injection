package proxycontrol;


import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import proxy.Proxy;

@Path("control")
public class ProxyControl {
	public static Proxy proxy;
	
	@GET
	@Path("status")
	@Produces(MediaType.TEXT_PLAIN)
	public String paramMethod(@QueryParam("name") String name) {
	  return "Proxy: " + (proxy != null ? (proxy.started ? "started" : "not started") : "null");
	}
	
	@POST
	@Path("post")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.TEXT_HTML)
	public void postMethod(@FormParam("name") String name) {
	  System.out.println("<h2>Hello, " + name + "</h2>");
	}
}