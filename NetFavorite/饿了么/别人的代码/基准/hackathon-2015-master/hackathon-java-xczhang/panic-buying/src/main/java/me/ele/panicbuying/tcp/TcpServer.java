package me.ele.panicbuying.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map.Entry;

public class TcpServer {
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    public TcpServer(GroupConfig boss, GroupConfig worker) {
        bossGroup = boss.getGroup();
        workerGroup = worker.getGroup();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
        bootBoss(boss);
        bootWorker(worker);
    }

    private void bootBoss(GroupConfig boss) {
        bootChannelHandler(boss, bootstrap::handler);
        bootOption(boss, bootstrap::option);
    }

    private void bootWorker(GroupConfig worker) {
        bootChannelHandler(worker, bootstrap::childHandler);
        bootOption(worker, bootstrap::childOption);
    }

    private void bootChannelHandler(GroupConfig gourp, ChannelHandlerOp op) {
        ChannelHandler channelHandler = gourp.getChannelHandler();
        if (channelHandler != null) {
            op.execute(channelHandler);
        }
    }

    private void bootOption(GroupConfig gourp, OptionOp op) {
        for (Entry<ChannelOption<?>, Object> entry : gourp.getOptions().entrySet()) {
            bootOption(entry.getKey(), entry.getValue(), op);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void bootOption(ChannelOption<T> option, Object value, OptionOp op) {
        op.execute(option, (T) value);
    }

    public void start(int port) throws Exception {
        start(() -> {
            return bootstrap.bind(port);
        });
    }

    public void start(String host, int port) throws Exception {
        start(() -> {
            return bootstrap.bind(host, port);
        });
    }

    private void start(BindOp op) throws Exception {
        try {
            op.execute().sync();
        } catch (Throwable e) {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            throw e;
        }
    }

    public void tryStart(int port) throws Exception {
        tryStart(() -> {
            return bootstrap.bind(port);
        });
    }

    public void tryStart(String host, int port) throws Exception {
        tryStart(() -> {
            return bootstrap.bind(host, port);
        });
    }

    private void tryStart(BindOp op) throws Exception {
        op.execute().sync();
    }

    @FunctionalInterface
    private interface ChannelHandlerOp {
        void execute(ChannelHandler channelHandler);
    }

    @FunctionalInterface
    private interface OptionOp {
        <T> void execute(ChannelOption<T> option, T value);
    }

    @FunctionalInterface
    private interface BindOp {
        ChannelFuture execute();
    }
}
