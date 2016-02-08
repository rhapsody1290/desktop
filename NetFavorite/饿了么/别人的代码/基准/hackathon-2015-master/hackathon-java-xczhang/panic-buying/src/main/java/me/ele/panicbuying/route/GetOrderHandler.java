package me.ele.panicbuying.route;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import me.ele.panicbuying.http.HttpRequestHandler;
import me.ele.panicbuying.http.OneResponseHandler;
import me.ele.panicbuying.utils.LoadDB;
import me.ele.panicbuying.utils.Redispipe;
import me.ele.panicbuying.utils.StaticResponse;
import me.ele.panicbuying.utils.Stock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xczhang on 15/10/30 下午3:50.
 */
public class GetOrderHandler extends OneResponseHandler {
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
        Jedis jedis = Redispipe.getJedis();
        String cart_id = jedis.get(token);
        if (cart_id == null) return StaticResponse.OK.copy();
        String order = jedis.get(cart_id);
        String responseJson = "[{\"id\":\"" + cart_id + "\",\"items\": [";
        Map<Integer, Integer> order_map = new HashMap<>();
        int food_id = 0;
        for (int i = 0; i < order.length(); i++) {
            food_id = food_id * 10 + (order.charAt(i) - '0');
            if (i % 3 == 2) {
                order_map.put(food_id, order_map.getOrDefault(food_id, 0) + 1);
                food_id = 0;
            }

        }

        int total = 0;
        int x = order_map.size();
        for (Map.Entry<Integer, Integer> entry : order_map.entrySet()) {
            x--;
            responseJson = responseJson + "{\"food_id\":" + entry.getKey() + ",\"count\":" + entry.getValue() + "}";
            total = total + entry.getValue() * Stock.getPrice(entry.getKey());
            if (x != 0) responseJson = responseJson + ",";
        }
        responseJson = responseJson + "],\"total\":" + total + "}]";

        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseJson.getBytes()));
        fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
        return (fullHttpResponse);


    }
}
