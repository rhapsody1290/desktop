package me.ele.draenor.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

public class JedisManager {

	private static JedisManager instnce = new JedisManager();

	private final JedisFactory factory;

	private ThreadLocal<Jedis> pool = new ThreadLocal<Jedis>() {
		@Override
		protected Jedis initialValue() {
			try {
				return factory.makeObject();
			} catch (Exception e) {
			}
			return null;
		}
	};

	private JedisManager() {
		String host = System.getenv("REDIS_HOST");
		String port = System.getenv("REDIS_PORT");

		factory = new JedisFactory(host == null ? "" : host, port == null ? 6379 : Integer.valueOf(port),
				Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
	}

	public static JedisManager getInstance() {
		return instnce;
	}

	public Jedis getJedis() {
		return pool.get();
	}

	public void close() {
		// nothing to do
		// need to close when system out gracefully
	}
}
