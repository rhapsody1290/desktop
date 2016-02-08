package me.ele.panicbuying.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class ChInitializer extends ChannelInitializer<Channel> {
    private final IChInitializer initializer;

    public ChInitializer(IChInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        initializer.initChannel(ch);
    }
}
