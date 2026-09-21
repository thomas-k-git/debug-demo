package com.dynatrace.debugdemo.backend;

import com.github.luben.zstd.Zstd;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class BackendApp {
    private static final Logger log = LoggerFactory.getLogger(BackendApp.class);

    public static void main(String[] args) throws Exception {
        startZstdCompressor(Runtime.getRuntime().availableProcessors());

        Server server = new Server(8081);

        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");
        ctx.addServlet(BackendHandler.class, "/*");

        server.setHandler(ctx);
        server.start();
        log.info("Backend listening on http://localhost:8081");
        server.join();
    }

    private static void startZstdCompressor(int count) {
        for (int i = 0; i < count; i++) {
            int idx = i;
            Thread t = new Thread(() -> {
                Random rng = new Random();
                // Zstd.compress() pins the Java heap arrays via GetPrimitiveArrayCritical for
                // the duration of the native call. While any thread holds a JNI critical section
                // the GCLocker blocks all collections — stall duration = slowest thread to exit.
                // 32 MB input at level 19 keeps each thread in native code long enough that
                // with numCPU threads running concurrently the GCLocker stall becomes visible.
                while (true) {
                    byte[] input = new byte[32 * 1024 * 1024 - 1];
                    rng.nextBytes(input); // random bytes defeat compression, maximising CPU time in JNI
                    byte[] output = new byte[(int) Zstd.compressBound(input.length)];
                    long compressedSize = Zstd.compress(output, input, 19); // level 19 = slowest/max
                    log.info("zstd-compressor-{} {} MB -> {} bytes", idx, (long)input.length >> 20, compressedSize);
                }
            }, "zstd-compressor-" + idx);
            t.setDaemon(true);
            t.start();
        }
    }
}
