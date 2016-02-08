package me.ele.panicbuying.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class HttpServerUrlHandler extends HttpServerHandler {
    private final HttpRequestHandler defaultHandler;
    private final Map<String, HttpRequestHandler> handlers = new HashMap<>();

    public HttpServerUrlHandler(HttpRequestHandler defaultHandler) {
        if (defaultHandler == null) {
            throw new NullPointerException("Default handler should not be null!");
        }
        this.defaultHandler = defaultHandler;
    }

    public HttpServerUrlHandler register(HttpMethod method, String uri, HttpRequestHandler handler) {
        String key = genKey(method, uri);
        if (handlers.put(key, handler) != null) {
            throw new IllegalArgumentException("Handler already registered for " + key + "!");
        }
        return this;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, HttpRequest httpRequest, ByteBuf byteBuf) throws Exception {

        QueryStringDecoder queryDecoder = new QueryStringDecoder(httpRequest.getUri());
        String key = genKey(httpRequest.getMethod(), queryDecoder.path());
        if (httpRequest.getMethod() == HttpMethod.PATCH) {

            List<String> x = new ArrayList<>();
            x.add(key.replace("PATCH@/carts/", ""));
            Map<String, List<String>> params = new HashMap<>();
            params.put("cart_id", x);
            if (queryDecoder.parameters().containsKey("access_token")) {
                x = new ArrayList<>();
                x.add(queryDecoder.parameters().get("access_token").get(0));
                params.put("access_token", x);
            }
            key = "PATCH@/carts";

            handlers.getOrDefault(key, defaultHandler).handle(ctx, httpRequest, params, byteBuf);

            return;
        }

        handlers.getOrDefault(key, defaultHandler).handle(ctx, httpRequest, queryDecoder.parameters(), byteBuf);

        return;
    }

    private String genKey(HttpMethod method, String uri) {
        return method.name() + "@" + uri.toLowerCase();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
