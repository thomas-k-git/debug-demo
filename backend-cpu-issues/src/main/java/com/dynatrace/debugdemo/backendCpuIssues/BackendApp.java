package com.dynatrace.debugdemo.backendCpuIssues;

import com.github.luben.zstd.Zstd;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class BackendApp {
	private static final Logger log = LoggerFactory.getLogger(BackendApp.class);

	public static void main(String[] args) throws Exception {
		startRandomAllocator(1);
		startBigAllocator();
		keepCpusBusy(Runtime.getRuntime().availableProcessors() / 2);
		startZstdCompressor(1);

		Server server = new Server(8081);

		ServletContextHandler ctx = new ServletContextHandler();
		ctx.setContextPath("/");
		ctx.addServlet(BackendHandler.class, "/*");

		server.setHandler(ctx);
		server.start();
		log.info("Backend listening on http://localhost:8081");
		server.join();
	}

	private static void keepCpusBusy(int count) {
		// heavy cpu load for all but one CPU
		for (int i = 0; i < count; i++) {
			var t = new Thread(() -> {
				double sum = 1.6526;
				while (true) {
					sum *= 3;
					if (sum == 1234.0f) {
						System.out.println("unlikely black hole to prevent code elimination");
					}
				}
			}, "doCalc");
			t.setDaemon(true);
			t.start();
		}
	}

	private static void startRandomAllocator(int count) {
		for (int i = 0; i < count; i++) {
			Thread t = new Thread(() -> {
				try {
					while (true) {
						var pseudo = new Random();
						var newarray = new byte[100_000];
						newarray[3] = (byte) pseudo.nextLong();
						if (pseudo.nextLong() == 23L) {
							System.out.println(newarray[3]);
						}
					}
				} catch (Error e) {
					System.out.println("allocator OOM");
					throw e;
				}
			}, "allocator" + i);
			t.setDaemon(true);
			t.start();
		}
	}

	private static void startBigAllocator() {
		var t = new Thread(() -> {
			try {
				while (true) {
					var pseudo = new Random();
					var newarray = new byte[400_000_000];
					newarray[3] = (byte) pseudo.nextLong();
					if (pseudo.nextLong() == 23L) {
						System.out.println(newarray[3]);
					}
					try {
						Thread.sleep(3_000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			} catch (Error e) {
				System.out.println("allocator OOM");
				throw e;
			}
		}, "bigallocator");
		t.setDaemon(true);
		t.start();
	}

	private static void startZstdCompressor(int count) {
		for (int i = 0; i < count; i++) {
			int idx = i;
			Thread t = new Thread(() -> {
				// Zstd.compress() pins the Java heap arrays via GetPrimitiveArrayCritical for
				// the duration of the native call. While any thread holds a JNI critical section
				// the GCLocker blocks all collections — stall duration = slowest thread to exit.
				// 32 MB input at level 19 keeps each thread in native code long enough that
				// with numCPU threads running concurrently the GCLocker stall becomes visible.
				while (true) {
					try {
						Random rng = new Random();
						int inputSize = 200_000_000;
						int arraySize = (int) Zstd.compressBound(inputSize);
						// memory consumption constant - irrelevant for region locking
						// new objects, to avoid a stable old gen
						byte[] input = new byte[inputSize];
						byte[] output = new byte[arraySize];

						rng.nextBytes(input); // random bytes defeat compression, maximising CPU time in JNI

						long compressedSize = Zstd.compress(output, input, Zstd.maxCompressionLevel());
						log.info("zstd-compressor-{} {} MB -> {} bytes", idx, (long) input.length >> 20, compressedSize);
					} catch (OutOfMemoryError e) {
						System.out.println("zstd OOM");
						throw e;
					}
				}
			}, "zstd-compressor-" + idx);
			t.setDaemon(true);
			t.start();
		}
	}
}
