package com.dynatrace.backend;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BackendHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=utf-8");
        switch (req.getRequestURI()) {
            case "/hello" -> {
                resp.setStatus(200);
                resp.getWriter().write("Hello from backend!");
            }
            case "/error" -> {
                resp.setStatus(500);
                resp.getWriter().write("Something went wrong");
            }
            default -> resp.setStatus(404);
        }
    }
}
