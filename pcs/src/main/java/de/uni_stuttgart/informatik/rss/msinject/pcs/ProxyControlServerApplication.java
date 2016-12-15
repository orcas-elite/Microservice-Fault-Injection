package de.uni_stuttgart.informatik.rss.msinject.pcs;

import de.uni_stuttgart.informatik.rss.msinject.pcs.resources.ProxyResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ProxyControlServerApplication extends Application<ProxyControlServerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ProxyControlServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "ProxyControlServer";
    }

    @Override
    public void initialize(final Bootstrap<ProxyControlServerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final ProxyControlServerConfiguration configuration,
                    final Environment environment) {

        environment.jersey().register(new ProxyResource());
    }

}
