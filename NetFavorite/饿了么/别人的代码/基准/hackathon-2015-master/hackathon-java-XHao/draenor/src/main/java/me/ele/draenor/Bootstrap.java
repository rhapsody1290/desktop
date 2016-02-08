package me.ele.draenor;

import me.ele.draenor.server.config.ServerConfig;
import me.ele.draenor.server.netty.WebServer;

public class Bootstrap {

	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		try {
			new WebServer().start(config);
		} catch (Exception e) {
			System.err.println("Bootstrap fail to start.");
			System.exit(1);
		}
	}

}
