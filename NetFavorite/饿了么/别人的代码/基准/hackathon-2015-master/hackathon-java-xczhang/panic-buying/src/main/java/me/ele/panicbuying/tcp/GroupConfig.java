package me.ele.panicbuying.tcp;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.HashMap;
import java.util.Map;

public class GroupConfig {
    private int threads;
    private ChannelHandler channelHandler;
    private Map<ChannelOption<?>, Object> options = new HashMap<>();

    public GroupConfig setTheads(int threads) {
        if (threads < 0) {
            throw new IllegalArgumentException("Threads cannot be negative:" + threads + "!");
        }
        this.threads = threads;
        return this;
    }

    public GroupConfig setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
        return this;
    }

    public <T> GroupConfig setOption(ChannelOption<T> option, T value) {
        options.put(option, value);
        return this;
    }

    public EventLoopGroup getGroup() {
        return threads == 0 ? new NioEventLoopGroup() : new NioEventLoopGroup(threads);
    }

    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public Map<ChannelOption<?>, Object> getOptions() {
        return options;
    }
}
