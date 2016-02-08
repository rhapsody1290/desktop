package me.ele.draenor.util;

import java.util.concurrent.atomic.AtomicReference;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

public class JedisFactory {
	private final AtomicReference<HostAndPort> hostAndPort = new AtomicReference<HostAndPort>();
	private final int connectionTimeout;
	private final int soTimeout;
	private final String password;
	private final int database;
	private final String clientName;

	public JedisFactory(final String host, final int port, final int connectionTimeout, final int soTimeout,
			final String password, final int database, final String clientName) {
		this.hostAndPort.set(new HostAndPort(host, port));
		this.connectionTimeout = connectionTimeout;
		this.soTimeout = soTimeout;
		this.password = password;
		this.database = database;
		this.clientName = clientName;
	}

	public Jedis makeObject() throws Exception {
		final HostAndPort hostAndPort = this.hostAndPort.get();
		final Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), connectionTimeout, soTimeout);

		jedis.connect();
		if (null != this.password) {
			jedis.auth(this.password);
		}
		if (database != 0) {
			jedis.select(database);
		}
		if (clientName != null) {
			jedis.clientSetname(clientName);
		}

		return jedis;
	}

}
