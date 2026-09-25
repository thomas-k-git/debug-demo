package com.dynatrace.debugdemo.backend;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class BackendHandler extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(BackendHandler.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        resp.setContentType("text/plain;charset=utf-8");
        switch (uri) {
            case "/hello" -> {
                log.info("GET {} -> 200", uri);
                resp.setStatus(200);
                resp.getWriter().write("Hello from backendGcIssues!");
            }
            case "/error" -> {
                log.error("GET {} -> throwing demo error", uri);
                throw new IOException("Demo example backendGcIssues error");
            }
            default -> {
                log.warn("GET {} -> 404", uri);
                resp.setStatus(404);
            }
        }
    }
}
