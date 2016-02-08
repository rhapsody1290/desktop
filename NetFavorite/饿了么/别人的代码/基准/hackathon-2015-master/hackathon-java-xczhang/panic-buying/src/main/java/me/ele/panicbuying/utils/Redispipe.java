package me.ele.panicbuying.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by xczhang on 15/11/24 下午4:25.
 */
public class Redispipe {
    private static JedisPool jedisPool=new JedisPool(Config.REDIS_HOST,Integer.parseInt(Config.REDIS_PORT));
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    public static void returnJedis(Jedis jedis){
        jedisPool.returnResource(jedis);
    }

}
