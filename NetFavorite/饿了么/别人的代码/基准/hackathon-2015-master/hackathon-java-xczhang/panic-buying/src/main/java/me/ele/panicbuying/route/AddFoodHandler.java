package me.ele.panicbuying.route;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import me.ele.panicbuying.http.HttpRequestHandler;
import me.ele.panicbuying.http.OneResponseHandler;
import me.ele.panicbuying.utils.FoodPost;
import me.ele.panicbuying.utils.LoadDB;
import me.ele.panicbuying.utils.Redispipe;
import me.ele.panicbuying.utils.StaticResponse;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by xczhang on 15/10/30 下午3:50.
 */
public class AddFoodHandler extends OneResponseHandler {
    private Gson gson=new Gson();
    @Override
    public FullHttpResponse handle( HttpRequest httpRequest,
                       Map<String, List<String>> params, ByteBuf byteBuf) {
        String requestJson = byteBuf.toString(Charset.forName("UTF-8"));


        if (requestJson.isEmpty()) {
            return (StaticResponse.EMPTY_REQUEST.copy());
        }
        if (!requestJson.contains("{")) {
            return (StaticResponse.MALFORMED_JSON.copy());
        }
        String token = "";

        if (httpRequest.headers().contains("Access-Token"))
            token = httpRequest.headers().get("Access-Token");
        if (params.containsKey("access_token"))
            token = params.get("access_token").get(0);
        if (!LoadDB.check_token(token)) {
            return (StaticResponse.INVALID_ACCESS_TOKEN.copy());
        }

        String cart_id = params.get("cart_id").get(0);

        if (!cart_id.contains(token)) {
            if (cart_id.toUpperCase() == cart_id.toLowerCase()) return (StaticResponse.CART_NOT_FOUND.copy());
            else {
                return (StaticResponse.NOT_AUTHORIZED_TO_ACCESS_CART.copy());
            }

        }
        return (parse(requestJson, cart_id));

    }

    public FullHttpResponse parse(String requestJson, String cart_id) {

        int food_id = 0;
        int count = 0;
        FoodPost foodPost=gson.fromJson(requestJson,FoodPost.class);
        food_id=foodPost.food_id;
        count=foodPost.count;
        if (food_id<0) return StaticResponse.FOOD_NOT_FOUND.copy();

        if (!LoadDB.food_stock.containsKey(food_id)) return StaticResponse.FOOD_NOT_FOUND.copy();
        //todo lua
        Jedis jedis = Redispipe.getJedis();
        if (count >= 0) {
            String tmp = "";
            if (food_id < 100) tmp = tmp + "0";
            if (food_id < 10) tmp = tmp + "0";
            tmp = tmp + food_id;
            Pipeline pipeline = jedis.pipelined();
            for (int i = 0; i < count; i++)
                pipeline.append(cart_id, tmp);
            List<Object> response = pipeline.syncAndReturnAll();
            for (Object o : response) {
                Long l = (Long) o;
                if (l > 9) {
                    Redispipe.returnJedis(jedis);
                    return StaticResponse.FOOD_OUT_OF_LIMIT.copy();
                }
            }
        }
        Redispipe.returnJedis(jedis);
        return StaticResponse.NO_CONTENT.copy();
    }
}