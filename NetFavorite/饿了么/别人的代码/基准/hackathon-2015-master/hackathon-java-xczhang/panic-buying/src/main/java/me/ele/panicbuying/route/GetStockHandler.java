package me.ele.panicbuying.route;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import me.ele.panicbuying.http.HttpRequestHandler;
import me.ele.panicbuying.http.OneResponseHandler;
import me.ele.panicbuying.utils.LoadDB;
import me.ele.panicbuying.utils.StaticResponse;
import me.ele.panicbuying.utils.Stock;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xczhang on 15/10/30 下午3:48.
 */
public class GetStockHandler extends OneResponseHandler {
    @Override
    public FullHttpResponse handle(HttpRequest httpRequest,
                       Map<String, List<String>> params, ByteBuf byteBuf) {

        String token = "";
        if (httpRequest.headers().contains("Access-Token"))
            token = httpRequest.headers().get("Access-Token");
        if (params.containsKey("access_token"))
            token = params.get("access_token").get(0);

        if (!LoadDB.check_token(token)) {
            return (StaticResponse.INVALID_ACCESS_TOKEN.copy());
        }

        return (httpResponse(getJson_200(Stock.getStock())));
    }

    private static FullHttpResponse httpResponse(String responseBody) {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBody.getBytes()));
        fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
        return fullHttpResponse;
    }

    private static String getJson_200(Map<Integer, AtomicInteger> stock) {
        String json = "[";
        int k = stock.size();
        for (Map.Entry<Integer, AtomicInteger> entry : stock.entrySet()) {
            json = json + "{\"id\":" + entry.getKey() + ",\"price\":" + Stock.getPrice(entry.getKey()) + ",\"stock\":" + entry.getValue().get() + "}";
            k--;
            if (k != 0) json = json + ',';
        }

        json = json + "]";
        return json;
    }

}
