package com.dynatrace.debugdemo.frontend;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontendApp {
    private static final Logger log = LoggerFactory.getLogger(FrontendApp.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");
        ctx.addServlet(FrontendHandler.class, "/*");

        server.setHandler(ctx);
        server.start();
        log.info("Frontend listening on http://localhost:8080 -> http://localhost:8081");
        server.join();
    }
}
