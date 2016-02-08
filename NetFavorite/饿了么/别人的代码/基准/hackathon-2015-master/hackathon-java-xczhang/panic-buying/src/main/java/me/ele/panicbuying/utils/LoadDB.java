package me.ele.panicbuying.utils;

import redis.clients.jedis.Jedis;
import redis.clients.util.RedisInputStream;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xczhang on 15/11/12 下午3:19.
 */

public class LoadDB {
    public static Map<String, String> user_map = new ConcurrentHashMap<>();
    public static Map<String, Integer> user_id = new ConcurrentHashMap<>();
    public static Map<Integer, Integer> food_stock = new ConcurrentHashMap<>();
    public static Map<Integer, Integer> food_price = new ConcurrentHashMap<>();
    public static long card_mod = 0;


    public static void load_user_msg() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + Config.DB_HOST + ":" + Config.DB_PORT + "/" + Config.DB_NAME, Config.DB_USER, Config.DB_PASS);
        Statement statement = null;
        ExecutorService executor = Executors.newFixedThreadPool(1);
        connection.setNetworkTimeout(executor, 1000);
        statement = connection.createStatement();
        statement.execute("SELECT id, name, password FROM user;");
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            user_map.put(resultSet.getString("name"), resultSet.getString("password"));
            user_id.put(resultSet.getString("name"), Integer.parseInt(resultSet.getString("id")));
            //System.out.println(resultSet.getString("name") + ' ' + resultSet.getString("password"));
        }


        connection.close();
        statement.close();
        resultSet.close();
    }

    public static void load_food_msg() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + Config.DB_HOST + ":" + Config.DB_PORT + "/" + Config.DB_NAME, Config.DB_USER, Config.DB_PASS);
        Jedis jedis = new Jedis(Config.REDIS_HOST, Integer.parseInt(Config.REDIS_PORT));
        Statement statement = null;
        ExecutorService executor = Executors.newFixedThreadPool(1);
        connection.setNetworkTimeout(executor, 1000);
        statement = connection.createStatement();
        statement.execute("SELECT id, stock, price FROM food;");
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            food_stock.put(resultSet.getInt("id"), resultSet.getInt("stock"));
            food_price.put(resultSet.getInt("id"), resultSet.getInt("price"));
            jedis.set("" + resultSet.getInt("id"), "" + resultSet.getInt("stock"));
            Stock.default_stock[resultSet.getInt("id")] = resultSet.getInt("stock");
            Stock.setStock(resultSet.getInt("id"), resultSet.getInt("stock"));
            Stock.setPrice(resultSet.getInt("id"), resultSet.getInt("price"));

            //System.out.println(""+resultSet.getInt("id")+" "+resultSet.getInt("stock"));
        }
        card_mod = jedis.incr("card_mod");
        Cart.setCartMode((int) card_mod);
        jedis.close();
        connection.close();
        statement.close();
        resultSet.close();
    }


    public static boolean check_user(String username, String password) {
        if (!user_id.containsKey(username)) return false;
        return user_map.get(username).equals(password);
    }

    public static boolean check_token(String token) {
        if (token.isEmpty()) return false;
        String tmp = new StringBuffer(token).reverse().toString();
        return user_id.containsKey(tmp);
    }

}
