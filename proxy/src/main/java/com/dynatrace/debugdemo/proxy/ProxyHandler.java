package com.dynatrace.debugdemo.proxy;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import java.io.IOException;

public class ProxyHandler extends HttpServlet {

    private static final String BACKEND = "http://localhost:8081";
    private CloseableHttpClient httpClient;

    @Override
    public void init() {
        httpClient = HttpClients.createDefault();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String qs = req.getQueryString();
        String target = BACKEND + req.getRequestURI() + (qs != null ? "?" + qs : "");

        httpClient.execute(new HttpGet(target), (ClassicHttpResponse backendResp) -> {
            resp.setStatus(backendResp.getCode());
            HttpEntity entity = backendResp.getEntity();
            if (entity != null) {
                entity.writeTo(resp.getOutputStream());
            }
            return null;
        });
    }

    @Override
    public void destroy() {
        try {
            httpClient.close();
        } catch (IOException ignored) {}
    }
}
