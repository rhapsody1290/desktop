package me.ele.draenor.server.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

class ServerBootstrapFactory {

	private static final Logger logger = LoggerFactory.getLogger(ServerBootstrapFactory.class);

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public ServerBootstrap newServerBootstrap() {
		ServerBootstrap serverBootstrap = null;
		try {
			serverBootstrap = newServerBootstrap(0, 0);
		} catch (IllegalArgumentException e) {
			// NOP
		}
		return serverBootstrap;
	}

	public ServerBootstrap newServerBootstrap(int acceptor, int ioThread) throws IllegalArgumentException {
		if (acceptor < 0 || ioThread < 0) {
			throw new IllegalArgumentException("acceptor/ioThread number < 0");
		}

		if (Epoll.isAvailable()) {
			logger.info("Platform is {}, use EpollEventLoopGroup", System.getProperties().getProperty("os.name"));
			return newEpollServerBootstrap(acceptor, ioThread);
		}

		logger.info("Platform is {}, use NioEventLoopGroup", System.getProperties().getProperty("os.name"));
		return newNioServerBootstrap(acceptor, ioThread);
	}

	public void recycle() {
		logger.info("ServerBootstrap start to recycle its resources: workgroup and bossGroup start to shutdown......");
		if (workerGroup != null)
			workerGroup.shutdownGracefully();
		if (bossGroup != null)
			bossGroup.shutdownGracefully();
	}

	private ServerBootstrap newNioServerBootstrap(int acceptor, int ioThread) {
		bossGroup = new NioEventLoopGroup(acceptor);
		workerGroup = new NioEventLoopGroup(ioThread);
		return new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
	}

	private ServerBootstrap newEpollServerBootstrap(int acceptor, int ioThread) {
		bossGroup = new EpollEventLoopGroup(acceptor);
		workerGroup = new EpollEventLoopGroup(ioThread);
		return new ServerBootstrap().group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class);
	}
}
