package com.dynatrace.debugdemo.backend;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BackendHandler extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(BackendHandler.class);
    private final List<MyData> storage = new LinkedList<>();

    private static final String FORM_HTML = """
            <html>
            <head><title>Demo Form</title></head>
            <body>
              <h2>Demo Form</h2>
              <form method="POST" action="/submit">
                <label>Name:<br><input type="text" name="name" placeholder="Your name"></label><br><br>
                <label>Message:<br><textarea name="message" rows="3" cols="40" placeholder="Your message"></textarea></label><br><br>
                <label>Priority:
                  <select name="priority">
                    <option value="low">Low</option>
                    <option value="medium" selected>Medium</option>
                    <option value="high">High</option>
                  </select>
                </label><br><br>
                <button type="submit" name="action" value="save">Save</button>
                <button type="submit" name="action" value="send">Send</button>
                <button type="submit" name="action" value="discard">Discard</button>
              </form>
            </body>
            </html>
            """;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        resp.setContentType("text/html;charset=utf-8");
        switch (uri) {
            case "/hello" -> {
                log.info("GET {} -> 200", uri);
                storage.add(new MyData());
                resp.setStatus(200);
                resp.getWriter().write("<html><body>Hello from backend!</body></html>");
            }
            case "/form" -> {
                log.info("GET {} -> 200", uri);
                resp.setStatus(200);
                resp.getWriter().write(FORM_HTML);
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        resp.setContentType("text/html;charset=utf-8");
        if ("/submit".equals(uri)) {
            String name     = req.getParameter("name");
            String message  = req.getParameter("message");
            String priority = req.getParameter("priority");
            String action   = req.getParameter("action");
            log.info("POST /submit: action={}, name={}, priority={}, message={}", action, name, priority, message);
            resp.setStatus(200);
            resp.getWriter().write("""
                    <html><body>
                      <p>Received: action=<b>%s</b>, name=<b>%s</b>, priority=<b>%s</b></p>
                      <a href="/form">Back</a>
                    </body></html>
                    """.formatted(action, name, priority));
        } else {
            log.warn("POST {} -> 404", uri);
            resp.setStatus(404);
        }
    }
}
