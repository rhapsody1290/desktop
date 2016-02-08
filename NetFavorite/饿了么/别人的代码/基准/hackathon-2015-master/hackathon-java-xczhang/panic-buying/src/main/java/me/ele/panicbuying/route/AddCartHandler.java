package me.ele.panicbuying.route;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import me.ele.panicbuying.http.HttpRequestHandler;
import me.ele.panicbuying.http.OneResponseHandler;
import me.ele.panicbuying.utils.Cart;
import me.ele.panicbuying.utils.LoadDB;
import me.ele.panicbuying.utils.StaticResponse;
import me.ele.panicbuying.utils.Stock;

import java.util.List;
import java.util.Map;

/**
 * Created by xczhang on 15/10/30 下午3:49.
 */
public class AddCartHandler extends OneResponseHandler {
    @Override
    public FullHttpResponse handle(HttpRequest httpRequest,
                                   Map<String, List<String>> params, ByteBuf byteBuf) {
        String token = "";
        if (httpRequest.headers().contains("Access-Token"))
            token = httpRequest.headers().get("Access-Token");
        if (params.containsKey("access_token"))
            token = params.get("access_token").get(0);

        if (!LoadDB.check_token(token)) {
            return (StaticResponse.INVALID_ACCESS_TOKEN).copy();
        }

        return (httpResponse(getJson_200(token)));


    }

    private static FullHttpResponse httpResponse(String responseBody) {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBody.getBytes()));
        fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
        return fullHttpResponse;
    }

    private static String getJson_200(String token) {
        String json = "{\"cart_id\":\"" + Cart.genCart(token) + "\"}";
        return json;
    }
}

