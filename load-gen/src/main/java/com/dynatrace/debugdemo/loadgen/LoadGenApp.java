package com.dynatrace.debugdemo.loadgen;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadGenApp {
    private static final Logger log = LoggerFactory.getLogger(LoadGenApp.class);

    private static final String BASE_URL = System.getProperty("target", "http://localhost:8080");
    private static final long INTERVAL_MS = Long.parseLong(System.getProperty("interval", "5000"));

    private static final List<String> ENDPOINTS = List.of("/hello", "/error");

    public static void main(String[] args) throws InterruptedException {
        log.info("Load generator firing at {} every {}ms", BASE_URL, INTERVAL_MS);

        CloseableHttpClient client = HttpClients.createDefault();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdownNow();
            try { client.close(); } catch (Exception ignored) {}
            log.info("Load generator stopped.");
        }));

        scheduler.scheduleAtFixedRate(() -> {
            for (String path : ENDPOINTS) {
                String url = BASE_URL + path;
                try {
                    client.execute(new HttpGet(url), (ClassicHttpResponse resp) -> {
                        String body = EntityUtils.toString(resp.getEntity());
                        log.info("GET {} -> {}  {}", path, resp.getCode(), body.strip());
                        return null;
                    });
                } catch (Exception e) {
                    log.error("GET {} -> ERROR: {}", path, e.getMessage());
                }
            }
        }, 0, INTERVAL_MS, TimeUnit.MILLISECONDS);

        Thread.currentThread().join();
    }
}
