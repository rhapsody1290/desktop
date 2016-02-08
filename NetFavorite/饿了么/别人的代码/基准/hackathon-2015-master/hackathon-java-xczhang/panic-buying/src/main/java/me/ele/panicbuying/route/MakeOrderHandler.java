package me.ele.panicbuying.route;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import me.ele.panicbuying.http.HttpRequestHandler;
import me.ele.panicbuying.http.OneResponseHandler;
import me.ele.panicbuying.utils.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by xczhang on 15/10/30 下午3:50.
 */
public class MakeOrderHandler extends OneResponseHandler {
    @Override
    public FullHttpResponse handle(HttpRequest httpRequest,
                                   Map<String, List<String>> params, ByteBuf byteBuf) {

        String requestJson = byteBuf.toString(Charset.forName("UTF-8"));
        if (requestJson.isEmpty()) {
            return StaticResponse.EMPTY_REQUEST.copy();

        }
        if (!requestJson.contains("{")) {
            return StaticResponse.MALFORMED_JSON.copy();

        }
        String token = "";
        if (httpRequest.headers().contains("Access-Token"))
            token = httpRequest.headers().get("Access-Token");
        if (params.containsKey("access_token"))
            token = params.get("access_token").get(0);

        if (!LoadDB.check_token(token)) {
            return (StaticResponse.INVALID_ACCESS_TOKEN.copy());
        }
        String cart_id = parse(requestJson);
        if (!cart_id.contains(token)) {
            if (LoadDB.user_map.containsKey(new StringBuffer(token).reverse().toString()))
                return (StaticResponse.NOT_AUTHORIZED_TO_ACCESS_CART.copy());
            else
                return (StaticResponse.CART_NOT_FOUND.copy());
        }

        //todo use lua
        Jedis jedis = Redispipe.getJedis();
        Pipeline pipeline = jedis.pipelined();
        pipeline.incr(token + "_");
        pipeline.get(token);
        pipeline.append(token, cart_id);
        pipeline.get(cart_id);
        List<Object> response = pipeline.syncAndReturnAll();
        if (!response.get(0).equals(new Long(1))) {

            if (response.get(1) != null) jedis.set(token, (String) response.get(1));
            else jedis.del(token);

            Redispipe.returnJedis(jedis);
            return (StaticResponse.ORDER_OUT_OF_LIMIT.copy());
        }


        String card_foods = (String) response.get(3);
        int food_id = 0;
        boolean order = true;
        for (int i = 0; i < card_foods.length(); i++) {
            food_id = food_id * 10 + (card_foods.charAt(i) - '0');
            if (i % 3 == 2) {
                order = order && Stock.setStock(food_id, -1);
                food_id = 0;
            }

        }

        if (!order) {
            if (response.get(1) != null) jedis.set(token, (String) response.get(1));
            else jedis.del(token);
            Redispipe.returnJedis(jedis);
            return (StaticResponse.FOOD_OUT_OF_STOCK.copy());

        }
        Redispipe.returnJedis(jedis);
        return (httpResponse(getJson_200(cart_id)));


    }

    private static FullHttpResponse httpResponse(String responseBody) {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBody.getBytes()));
        fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
        return fullHttpResponse;
    }

    private static String getJson_200(String cart_id) {
        return "{\"id\": \"" + cart_id + "\"}";
    }

    public Gson gson=new Gson();
    public String parse(String requestJson) {
        Order order=gson.fromJson(requestJson,Order.class);
        String cart_id = order.cart_id;
        return cart_id;
    }


}
