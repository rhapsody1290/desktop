package me.ele.panicbuying.server;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.concurrent.EventExecutorGroup;

import me.ele.panicbuying.http.DefaultHandler;
import me.ele.panicbuying.http.HttpServer;
import me.ele.panicbuying.http.HttpServerUrlHandler;
import me.ele.panicbuying.route.*;
import me.ele.panicbuying.utils.Config;
import me.ele.panicbuying.utils.LoadDB;
import me.ele.panicbuying.utils.Stock;

public class Server {


    public static void init() throws Exception {
        Config.load_config();
        Stock.init();
        LoadDB.load_user_msg();
        LoadDB.load_food_msg();

    }


    public static void main(String[] args) throws Exception {
        init();
        EventExecutorGroup eventExecutorGroup = new NioEventLoopGroup(16);
        /**
         * POST     /login
         * GET      /foods
         * POST     /carts
         * PATCH    /carts/cart_id
         * POST     /orders
         * GET      /orders
         * GET      /admin/orders
         */
        HttpServerUrlHandler httpServerUrlHandler = new HttpServerUrlHandler(new DefaultHandler(HttpResponseStatus.BAD_GATEWAY))
                .register(HttpMethod.POST, "/login", new LoginHandler())
                .register(HttpMethod.GET, "/foods", new GetStockHandler())
                .register(HttpMethod.POST, "/carts", new AddCartHandler())
                .register(HttpMethod.PATCH, "/carts", new AddFoodHandler())
                .register(HttpMethod.POST, "/orders", new MakeOrderHandler())
                .register(HttpMethod.GET, "/orders", new GetOrderHandler())
                .register(HttpMethod.GET, "/admin/orders", new AdminGetOrderHandler());


        new HttpServer(128, ch -> ch.pipeline().addLast(httpServerUrlHandler)).start(Integer.parseInt(Config.APP_PORT));


    }


}