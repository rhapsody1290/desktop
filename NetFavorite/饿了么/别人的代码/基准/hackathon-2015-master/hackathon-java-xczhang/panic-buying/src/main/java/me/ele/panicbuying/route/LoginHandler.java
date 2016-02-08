package me.ele.panicbuying.route;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import me.ele.panicbuying.http.HttpRequestHandler;
import me.ele.panicbuying.http.OneResponseHandler;
import me.ele.panicbuying.utils.LoadDB;
import me.ele.panicbuying.utils.StaticResponse;
import me.ele.panicbuying.utils.User;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by xczhang on 15/10/30 下午3:13.
 */
public class LoginHandler extends OneResponseHandler {
    Gson gson=new Gson();
    @Override
    protected FullHttpResponse handle(HttpRequest httpRequest, Map<String, List<String>> params, ByteBuf byteBuf) throws Exception {

        String requestJson = byteBuf.toString(Charset.forName("UTF-8"));
        if (requestJson.isEmpty()) {
            return StaticResponse.EMPTY_REQUEST.copy();
        }

        if (!requestJson.contains("{")) {
            return StaticResponse.MALFORMED_JSON.copy();
        }

        String user = parse(requestJson);

        if (user == null) {
            return StaticResponse.USER_AUTH_FAIL.copy();
        } else {
            return httpResponse(getJson_200(user));
        }

    }

    private static FullHttpResponse httpResponse(String responseBody) {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBody.getBytes()));
        fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
        return fullHttpResponse;
    }

    private static String getJson_200(String user) {
        return "{\"user_id\":" + LoadDB.user_id.get(user) + ",\"username\": \"" + user + "\",\"access_token\": \"" + new StringBuffer(user).reverse().toString() + "\"}";
    }


    public String parse(String requestJson) {
        User tmp=gson.fromJson(requestJson,User.class);
        String user=tmp.username;
        String pass=tmp.password;

        if (!user.isEmpty() && !pass.isEmpty() && LoadDB.check_user(user, pass))
            return user;
        else
            return null;
    }


}
