package com.example.proxy;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;

public class ProxyApp {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");
        ctx.addServlet(ProxyHandler.class, "/*");

        server.setHandler(ctx);
        server.start();
        System.out.println("Proxy listening on http://localhost:8080 -> http://localhost:8081");
        server.join();
    }
}
