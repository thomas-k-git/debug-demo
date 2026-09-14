package com.dynatrace.debugdemo.loadgen;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadGenApp {

    private static final String BASE_URL = System.getProperty("target", "http://localhost:8080");
    private static final long INTERVAL_MS = Long.parseLong(System.getProperty("interval", "2000"));
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private static final List<String> ENDPOINTS = List.of("/hello", "/error");

    public static void main(String[] args) throws InterruptedException {
        System.out.printf("Load generator firing at %s every %dms%n", BASE_URL, INTERVAL_MS);

        CloseableHttpClient client = HttpClients.createDefault();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdownNow();
            try { client.close(); } catch (Exception ignored) {}
            System.out.println("\nLoad generator stopped.");
        }));

        scheduler.scheduleAtFixedRate(() -> {
            for (String path : ENDPOINTS) {
                String url = BASE_URL + path;
                String time = LocalTime.now().format(TIME_FMT);
                try {
                    client.execute(new HttpGet(url), (ClassicHttpResponse resp) -> {
                        String body = EntityUtils.toString(resp.getEntity());
                        System.out.printf("[%s] GET %-20s -> %d  %s%n",
                                time, path, resp.getCode(), body.strip());
                        return null;
                    });
                } catch (Exception e) {
                    System.out.printf("[%s] GET %-20s -> ERROR: %s%n", time, path, e.getMessage());
                }
            }
        }, 0, INTERVAL_MS, TimeUnit.MILLISECONDS);

        // Block main thread; shutdown hook handles cleanup on Ctrl+C
        Thread.currentThread().join();
    }
}
