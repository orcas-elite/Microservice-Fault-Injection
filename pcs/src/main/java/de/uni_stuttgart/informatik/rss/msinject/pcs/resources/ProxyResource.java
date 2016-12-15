package de.uni_stuttgart.informatik.rss.msinject.pcs.resources;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/proxy")
@Produces(MediaType.APPLICATION_JSON)
public class ProxyResource {
	private final AtomicLong counter;

	public ProxyResource() {
		this.counter = new AtomicLong();
	}

	@GET
	@Timed
	public String sayHello() {
		return "sup #" + counter.getAndIncrement();
	}
}