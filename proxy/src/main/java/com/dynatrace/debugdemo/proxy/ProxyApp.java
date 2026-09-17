package com.dynatrace.debugdemo.proxy;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyApp {
    private static final Logger log = LoggerFactory.getLogger(ProxyApp.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");
        ctx.addServlet(ProxyHandler.class, "/*");

        server.setHandler(ctx);
        server.start();
        log.info("Proxy listening on http://localhost:8080 -> http://localhost:8081");
        server.join();
    }
}
