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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xczhang on 15/10/30 下午3:51.
 */
public class AdminGetOrderHandler extends OneResponseHandler {
    @Override
    public FullHttpResponse handle(HttpRequest httpRequest,
                                   Map<String, List<String>> params, ByteBuf byteBuf) {
        String token = "";
        if (httpRequest.headers().contains("Access-Token"))
            token = httpRequest.headers().get("Access-Token");
        if (params.containsKey("access_token"))
            token = params.get("access_token").get(0);

        if (!LoadDB.check_token(token) || !token.equals("toor")) {
            return (StaticResponse.INVALID_ACCESS_TOKEN.copy());
        }

        String responseJson = "[";
        Jedis jedis = Redispipe.getJedis();
        Set<String> tokens = jedis.keys("*_");

        try {
            int k = tokens.size() - 1;
            for (String token_ : tokens) {

                String token1 = token_.replace("_", "");
                String cart_id = jedis.get(token1);
                String order = null;
                if (cart_id != null) order = jedis.get(cart_id);
                if (order != null) {

                    responseJson = responseJson + "{\"id\":\"" + cart_id + "\",\"user_id\":" + LoadDB.user_id.get(new StringBuffer(token1).reverse().toString()) + ",\"items\": [";

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
                        if (entry.getValue()==2) entry.setValue(1);
                        responseJson = responseJson + "{\"food_id\":" + entry.getKey() + ",\"count\":" + entry.getValue() + "}";
                        total = total + entry.getValue() * Stock.getPrice(entry.getKey());
                        if (x != 0) responseJson = responseJson + ",";
                    }
                    responseJson = responseJson + "],\"total\":" + total + "}";
                    k--;
                    if (k > 0) responseJson = responseJson + ",";


                }
            }
            responseJson = responseJson + "]";
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseJson.getBytes()));
            fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
            return (fullHttpResponse);
        } catch (Exception e) {

        }
        return null;
    }
}
