package me.ele.draenor.server.netty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LifeCycle;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import me.ele.draenor.server.config.ServerConfig;
import me.ele.draenor.util.Cache;
import me.ele.draenor.util.JedisManager;

public class WebServer {

	private ServerConfig config;
	private final ServerBootstrapFactory factory = new ServerBootstrapFactory();
	private RouteHandler route;

	/**
	 * @throws Exception
	 */
	public void start(ServerConfig config) throws Exception {
		this.config = config;

		Cache.init();
		ServerBootstrap bootstrap = factory.newServerBootstrap(config.getMaxThreadSize(), 8);
		newRouteHandler();

		bootstrap.childHandler(new PipelineInitializer().setReguestHandler(route));
		parseChannelOption(bootstrap).bind(config.getPort()).sync().channel().closeFuture().sync();
	}

	private void newRouteHandler() {
		route = new RouteHandler();
	}

	private ServerBootstrap parseChannelOption(ServerBootstrap bootstrap) {
		bootstrap.option(ChannelOption.SO_BACKLOG, config.getQueue()).childOption(ChannelOption.SO_KEEPALIVE, true);
		return bootstrap;
	}

	public void recycle() {
		if (factory != null)
			factory.recycle();

		JedisManager.getInstance().close();
		((LifeCycle) LogManager.getContext()).stop();
	}

}
