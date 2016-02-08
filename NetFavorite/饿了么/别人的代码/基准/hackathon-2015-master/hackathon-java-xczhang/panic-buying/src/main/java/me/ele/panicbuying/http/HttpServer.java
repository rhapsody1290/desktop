package me.ele.panicbuying.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpServerCodec;
import me.ele.panicbuying.tcp.DefaultTcpServer;
import me.ele.panicbuying.tcp.IChInitializer;


public class HttpServer extends DefaultTcpServer {
    public HttpServer(int workerThreads, IChInitializer initializer) {
        super(workerThreads, new IChInitializer() {
            @Override
            public void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new HttpServerCodec());
                ch.pipeline().addLast(new HttpServerAggregator());
                initializer.initChannel(ch);
            }
        });
    }
}
