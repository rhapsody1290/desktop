package me.ele.panicbuying.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.List;
import java.util.Map;

public abstract class OneResponseHandler implements HttpRequestHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, HttpRequest httpRequest, Map<String, List<String>> params, ByteBuf byteBuf) throws Exception {
        FullHttpResponse response = handle(httpRequest, params, byteBuf);

        if (HttpHeaders.isKeepAlive(httpRequest)) {
            response.headers().set(HttpHeaders.Names.CONNECTION, Values.KEEP_ALIVE);
        }

        if (response.getStatus()!= HttpResponseStatus.OK && response.getStatus()!=HttpResponseStatus.NO_CONTENT )
            System.out.println(httpRequest.getUri()+"    "+response.getStatus());
        ChannelFuture future = ctx.writeAndFlush(response);
        if (!HttpHeaders.isKeepAlive(httpRequest)) {
            future.sync();
            ctx.close();
        }
    }

    protected abstract FullHttpResponse handle(HttpRequest httpRequest, Map<String, List<String>> params, ByteBuf byteBuf) throws Exception;
}
