package me.ele.draenor.server.config;

import lombok.Data;

@Data
public class ServerConfig {
	private int port = 8080;
	private int maxThreadSize = 500;
	private int queue = 128;
}
