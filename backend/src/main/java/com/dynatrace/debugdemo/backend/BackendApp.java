package com.dynatrace.debugdemo.backend;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackendApp {
	private static final Logger log = LoggerFactory.getLogger(BackendApp.class);

	public static void main(String[] args) throws Exception {
		Server server = new Server(8081);

		keepCpusBusy(2);

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
			}, "doCalc-" + i);
			t.setDaemon(true);
			t.start();
		}
	}

}
