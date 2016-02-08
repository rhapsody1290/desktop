package me.ele.panicbuying.utils;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;


import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xczhang on 15/11/24 下午3:30.
 */
public class StaticResponse {
    private static final Set<Integer> codeSet = new HashSet<Integer>() {
    };


    public static final FullHttpResponse NO_CONTENT;
    public static final FullHttpResponse OK;
    public static final FullHttpResponse EMPTY_REQUEST;
    public static final FullHttpResponse MALFORMED_JSON;
    public static final FullHttpResponse INVALID_ACCESS_TOKEN;
    public static final FullHttpResponse NOT_AUTHORIZED_TO_ACCESS_CART;
    public static final FullHttpResponse USER_AUTH_FAIL;
    public static final FullHttpResponse FOOD_OUT_OF_LIMIT;
    public static final FullHttpResponse FOOD_OUT_OF_STOCK;
    public static final FullHttpResponse ORDER_OUT_OF_LIMIT;
    public static final FullHttpResponse CART_NOT_FOUND;
    public static final FullHttpResponse FOOD_NOT_FOUND;


    static {


        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);


        codeSet.add(204);   //204   No content
        NO_CONTENT = fullHttpResponse;
        fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,Unpooled.wrappedBuffer("[]".getBytes()));
        fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());

        OK = fullHttpResponse;
        String msg;
        codeSet.add(4001);  //400-1  EMPTY_REQUEST
        msg = "{\"code\": \"EMPTY_REQUEST\",\"message\": \"请求体为空\"}";
        EMPTY_REQUEST = getResponse(HttpResponseStatus.BAD_REQUEST, msg);

        codeSet.add(4002);  //400-2  MALFORMED_JSON
        msg = "{\n" +
                "    \"code\": \"MALFORMED_JSON\",\n" +
                "    \"message\": \"格式错误\"\n" +
                "}";
        MALFORMED_JSON = getResponse(HttpResponseStatus.BAD_REQUEST, msg);

        codeSet.add(4011);  //401-1  INVALID_ACCESS_TOKEN
        msg = "{\n" +
                "    \"code\": \"INVALID_ACCESS_TOKEN\",\n" +
                "    \"message\": \"无效的令牌\"\n" +
                "}";
        INVALID_ACCESS_TOKEN = getResponse(HttpResponseStatus.UNAUTHORIZED, msg);


        codeSet.add(4012);  //401-2  NOT_AUTHORIZED_TO_ACCESS_CART
        msg = "{\n" +
                "    \"code\": \"NOT_AUTHORIZED_TO_ACCESS_CART\",\n" +
                "    \"message\": \"无权限访问指定的篮子\"\n" +
                "}";
        NOT_AUTHORIZED_TO_ACCESS_CART = getResponse(HttpResponseStatus.UNAUTHORIZED, msg);


        codeSet.add(4031);  //403-1  USER_AUTH_FAIL
        msg = "{\n" +
                "    \"code\": \"USER_AUTH_FAIL\",\n" +
                "    \"message\": \"用户名或密码错误\"\n" +
                "}";
        USER_AUTH_FAIL = getResponse(HttpResponseStatus.FORBIDDEN, msg);


        codeSet.add(4032);  //403-2  FOOD_OUT_OF_LIMIT
        msg = "{\"code\":\"FOOD_OUT_OF_LIMIT\",\"message\": \"篮子中食物数量超过了三个\"}";
        FOOD_OUT_OF_LIMIT = getResponse(HttpResponseStatus.FORBIDDEN, msg);


        codeSet.add(4033);  //403-3  FOOD_OUT_OF_STOCK
        msg = "{\n" +
                "    \"code\": \"FOOD_OUT_OF_STOCK\",\n" +
                "    \"message\": \"食物库存不足\"\n" +
                "}";
        FOOD_OUT_OF_STOCK = getResponse(HttpResponseStatus.FORBIDDEN, msg);


        codeSet.add(4034);  //403-4  ORDER_OUT_OF_LIMIT
        msg = "{\n" +
                "    \"code\": \"ORDER_OUT_OF_LIMIT\",\n" +
                "    \"message\": \"每个用户只能下一单\"\n" +
                "}";
        ORDER_OUT_OF_LIMIT = getResponse(HttpResponseStatus.FORBIDDEN, msg);


        codeSet.add(4041);  //404-1  CART_NOT_FOUND
        msg = "{\n" +
                "    \"code\": \"CART_NOT_FOUND\",\n" +
                "    \"message\": \"篮子不存在\"\n" +
                "}";
        CART_NOT_FOUND = getResponse(HttpResponseStatus.NOT_FOUND, msg);

        codeSet.add(4042);  //404-2  FOOD_NOT_FOUND
        msg = "{\n" +
                "    \"code\": \"FOOD_NOT_FOUND\",\n" +
                "    \"message\": \"食物不存在\"\n" +
                "}";
        FOOD_NOT_FOUND = getResponse(HttpResponseStatus.NOT_FOUND, msg);

    }


    private static FullHttpResponse getResponse(HttpResponseStatus status, String content) {
        FullHttpResponse fullHttpResponse = null;
        try {
            fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(content.getBytes(Charset.forName("UTF-8"))));
            fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
        } catch (Exception e) {

        }
        return fullHttpResponse;
    }


}
