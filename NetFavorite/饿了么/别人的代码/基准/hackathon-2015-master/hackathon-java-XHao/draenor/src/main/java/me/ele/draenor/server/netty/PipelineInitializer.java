package me.ele.draenor.server.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class PipelineInitializer extends ChannelInitializer<SocketChannel> {

	private static final int Max_Size = 1048576;// 1024*1024

	private ChannelHandler route;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(new HttpServerCodec());

		pipeline.addLast(new HttpObjectAggregator(Max_Size));

		pipeline.addLast(route);
	}

	public PipelineInitializer setReguestHandler(ChannelHandler routeHandler) {
		this.route = routeHandler;
		return this;
	}

}
