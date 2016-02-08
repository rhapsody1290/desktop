package me.ele.draenor.dao.impl;

import java.util.UUID;

import me.ele.draenor.dao.CartDao;
import me.ele.draenor.http.ExceptionResponse;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;
import me.ele.draenor.util.JedisManager;
import redis.clients.jedis.Jedis;

public class CartDaoImpl implements CartDao {

	private static final String authErr = "无权限访问指定的篮子";
	private static final String authErrCode = "NOT_AUTHORIZED_TO_ACCESS_CART";
	private static final String limitErr = "篮子中食物数量超过了三个";
	private static final String limitErrCode = "FOOD_OUT_OF_LIMIT";
	private static final String noCart = "篮子不存在";
	private static final String noCartCode = "CART_NOT_FOUND";

	static final String Table_cart = "cart:";

	static final String add_food_script = "local user_id = redis.call('get', 'cart:' .. KEYS[2])\n"
			+ "if not user_id then return '-1'\n end\n" + "if user_id ~= KEYS[1] then return '-2' end\n"
			+ "local total_count = redis.call('incrby', 'cart:count:' .. KEYS[2], KEYS[4])\n"
			+ "if total_count > 3 then\n" + "redis.call('set', 'cart:count:' .. KEYS[2], total_count - KEYS[4])\n"
			+ "return '-3' end\n" + "local cart_key = 'cart:detail:' .. KEYS[2] \n"
			+ "local after_add_count = redis.call('hincrby', cart_key, KEYS[3], KEYS[4])\n"
			+ "if after_add_count < 0 then\n " + "redis.call('hset', cart_key, KEYS[3], 0)\n"
			+ "total_count = total_count - after_add_count\n end\n"
			+ "redis.call('set', 'cart:count:' .. KEYS[2], total_count)\n" + "return 'ok'\n";

	static {
		JedisManager.getInstance().getJedis().scriptLoad(add_food_script);
	}

	@Override
	public Response create(String user_id) {
		String cart_id = UUID.randomUUID().toString().replaceAll("-", "");
		Jedis jedis = JedisManager.getInstance().getJedis();
		jedis.set(Table_cart + cart_id, user_id);
		StringBuilder sb = new StringBuilder(64);
		sb.append("{\"cart_id\":\"").append(cart_id).append("\"}");
		return new Response(Status.OK, sb.toString());
	}

	@Override
	public Response addFood(String user_id, String cart_id, String food_id, String count) {
		if (cart_id == null || cart_id.length() != 32) {
			return new ExceptionResponse(Status.NOT_FOUND, noCart, noCartCode);
		}
		// atomic
		Jedis jedis = JedisManager.getInstance().getJedis();
		String ret = (String) jedis.eval(add_food_script, 4, user_id, cart_id, food_id, count);
		if (ret.equals("ok")) {
			return new Response(Status.NO_CONTENT, "");
		} else if (ret.equals("-1")) {
			return new ExceptionResponse(Status.NOT_FOUND, noCart, noCartCode);
		} else if (ret.equals("-2")) {
			return new ExceptionResponse(Status.UNAUTHORIZED, authErr, authErrCode);
		} else {
			return new ExceptionResponse(Status.FORBIDDEN, limitErr, limitErrCode);
		}
	}
}
