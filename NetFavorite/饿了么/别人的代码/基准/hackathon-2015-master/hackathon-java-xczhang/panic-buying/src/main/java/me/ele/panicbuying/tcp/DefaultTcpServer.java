package me.ele.panicbuying.tcp;

import io.netty.channel.ChannelOption;

public class DefaultTcpServer extends TcpServer {
    public DefaultTcpServer(int workerThreads, IChInitializer initializer) {
        super(new GroupConfig().setTheads(1).setOption(ChannelOption.SO_BACKLOG, 128), new GroupConfig().setTheads(workerThreads)
                .setOption(ChannelOption.SO_KEEPALIVE, true).setOption(ChannelOption.TCP_NODELAY, true).setChannelHandler(new ChInitializer(initializer)));
    }
}
