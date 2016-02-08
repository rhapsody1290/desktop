package me.ele.panicbuying.utils;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xczhang on 15/11/24 下午4:24.
 */
public class Stock {
    private static final int stock_check_line = 700;
    public static int[] default_stock = new int[200];
    private static List<Integer> checkList = new ArrayList();
    private static Map<Integer, AtomicInteger> stock = new ConcurrentHashMap<>();
    private static int[] price = new int[101];
    private static Jedis jedis;

    public static void init() {
        jedis = new Jedis(Config.REDIS_HOST, Integer.parseInt(Config.REDIS_PORT));
    }


    public static Map<Integer, AtomicInteger> getStock() {
        for (Integer food : checkList) {
            stock.get(food).set((int) Long.parseLong(jedis.get("" + food)));
        }
        return stock;
    }

    public static boolean setStock(Integer food, int stock_num) {
        long x = 0;
        if (!stock.containsKey(food)) stock.put(food,new AtomicInteger(0));
        if (checkList.contains(food)) {
            x = jedis.decrBy("" + food, -stock_num);
            stock.get(food).set((int) x);
        } else
            x = stock.get(food).addAndGet(stock_num);
        if (x < 0) return false;
        if (!checkList.contains(food) && x < stock_check_line && x > 0) {
            checkList.add(food);
            long y=jedis.decrBy("" + food, default_stock[food] - x);
            stock.get(food).set((int) y);
        }
        if (x <= 0 && checkList.contains(food))
            checkList.remove(food);
        return true;
    }

    public static void setPrice(int food, int price_) {
        price[food] = price_;
    }

    public static int getPrice(int food) {
        return price[food];

    }
}
