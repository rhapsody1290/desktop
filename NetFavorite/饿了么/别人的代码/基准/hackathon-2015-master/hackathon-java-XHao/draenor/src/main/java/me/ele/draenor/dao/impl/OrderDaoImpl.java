package me.ele.draenor.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.ele.draenor.dao.OrderDao;
import me.ele.draenor.http.ExceptionResponse;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;
import me.ele.draenor.util.Cache;
import me.ele.draenor.util.JedisManager;
import redis.clients.jedis.Jedis;

public class OrderDaoImpl implements OrderDao {
	private static final String authErr = "无权限访问指定的篮子";
	private static final String authErrCode = "NOT_AUTHORIZED_TO_ACCESS_CART";
	private static final String noCart = "篮子不存在";
	private static final String noCartCode = "CART_NOT_FOUND";
	private static final String limitErr = "食物库存不足";
	private static final String limitErrCode = "FOOD_OUT_OF_STOCK";
	private static final String orderExistErr = "每个用户只能下一单";
	private static final String orderExistCode = "ORDER_OUT_OF_LIMIT";

	private static final String make_order_script = "local user_id = redis.call('get', 'cart:' .. KEYS[2])\n"
			+ "if not user_id then return '-1'\n end\n" + "if user_id ~= KEYS[1] then return '-2' end\n"
			+ "if redis.call('exists','order:' .. user_id) == 1 then return '-3' end\n"
			+ "local vals = redis.call('hgetall', 'cart:detail:' .. KEYS[2])\n" + "local i = 1\n"
			+ "while i < #vals do\n"
			+ "if redis.call('hincrby', 'food:stock', vals[i], 0 - vals[i+1]) < 0 then redis.call('del', 'order:temp:detail:' .. KEYS[2]) break\n"
			+ "else\n" + "redis.call('hset', 'order:temp:detail:' .. KEYS[2], vals[i], vals[i+1])" + "i = i + 2\n"
			+ "end\n" + "end\n" + "if i < #vals then\n" + "while i > 0 do\n"
			+ "redis.call('hincrby', 'food:stock', vals[i], vals[i+1])\n" + "i = i - 2\n" + "end\n" + "return '-4'\n"
			+ "else \n" + "redis.call('set', 'order:' .. user_id, KEYS[2])\n"
			+ "redis.call('rename', 'order:temp:detail:' .. KEYS[2], 'order:detail:' .. KEYS[2])\n" + "return 'ok'\n"
			+ "end\n";

	private static final String query_order_script = "local order_id = redis.call('get', 'order:' .. KEYS[1])\n"
			+ "if order_id then\n" + "local ret = redis.call('hgetall', 'order:detail:' .. order_id)\n"
			+ "ret[#ret + 1] = order_id\n" + " return ret end\n";

	static {
		JedisManager.getInstance().getJedis().scriptLoad(make_order_script);
		JedisManager.getInstance().getJedis().scriptLoad(query_order_script);
	}

	@Override
	public Response make(String user_id, String cart_id) {
		if (cart_id == null || cart_id.length() != 32) {
			return new ExceptionResponse(Status.UNAUTHORIZED, noCart, noCartCode);
		}
		// atomic
		Jedis jedis = JedisManager.getInstance().getJedis();
		String ret = (String) jedis.eval(make_order_script, 2, user_id, cart_id);
		if (ret.equals("ok")) {
			StringBuilder sb = new StringBuilder(64);
			sb.append("{\"id\":\"").append(cart_id).append("\"}").toString();
			return new Response(Status.OK, sb.toString());
		} else if (ret.equals("-1")) {
			return new ExceptionResponse(Status.NOT_FOUND, noCart, noCartCode);
		} else if (ret.equals("-2")) {
			return new ExceptionResponse(Status.UNAUTHORIZED, authErr, authErrCode);
		} else if (ret.equals("-3")) {
			return new ExceptionResponse(Status.FORBIDDEN, orderExistErr, orderExistCode);
		} else {
			return new ExceptionResponse(Status.FORBIDDEN, limitErr, limitErrCode);
		}

	}

	@Override
	public Response query(String user_id) {
		Jedis jedis = JedisManager.getInstance().getJedis();
		Object object = jedis.eval(query_order_script, 1, user_id);
		if (object == null) {
			return new Response(Status.OK, "[]");
		}

		@SuppressWarnings("unchecked")
		List<String> ret = (List<String>) object;
		StringBuilder sb = new StringBuilder(64);
		sb.append("[{");

		int i = 0;
		int total = 0;
		StringBuilder items = new StringBuilder(64);
		items.append("\"items\": [");
		boolean flag = false;

		while (i < ret.size()) {
			if (i == ret.size() - 1) {
				sb.append("\"id\": \"").append(ret.get(i++)).append("\"");
				sb.append(",");
			} else {
				String key = ret.get(i++);
				String val = ret.get(i++);
				int count = Integer.valueOf(val);
				if (count > 0) {
					total = total + Cache.getPrice(Integer.valueOf(key)) * count;
					if (flag) {
						items.append(",");
					} else {
						flag = true;
					}
					items.append("{\"food_id\":").append(key).append(",\"count\":").append(val).append("}");
				}
			}
		}

		sb.append(items.toString()).append("],").append("\"total\":").append(total);
		sb.append("}]");

		return new Response(Status.OK, sb.toString());
	}

	@Override
	public Response queryAll() {
		Jedis jedis = JedisManager.getInstance().getJedis();
		String[] users = Cache.getUsers();

		StringBuilder sb = new StringBuilder(128);
		boolean separator = false;
		sb.append("[");

		for (int i = 0; i < users.length; i++) {
			String orderId = jedis.get("order:" + users[i]);
			if (orderId != null) {
				Map<String, String> map = jedis.hgetAll("order:detail:" + orderId);
				StringBuilder items = new StringBuilder(64);
				items.append("{\"id\":\"").append(orderId).append("\",\"user_id\":").append(users[i]).append(",")
						.append("\"items\":[");
				boolean flag = false;
				int total = 0;

				for (Entry<String, String> pair : map.entrySet()) {
					if (flag) {
						items.append(",");
					} else {
						flag = true;
					}
					String key = pair.getKey();
					String val = pair.getValue();
					items.append("{\"food_id\":").append(key).append(",\"count\":").append(val).append("}");
					total = total + Integer.valueOf(val) * Cache.getPrice(Integer.valueOf(key));
				}
				if (total > 0) {
					if (separator) {
						sb.append(",");
					} else {
						separator = true;
					}
					items.append("],\"total\":").append(total).append("}");
					sb.append(items.toString());
				}
			}
		}

		sb.append("]");
		return new Response(Status.OK, sb.toString());
	}
}
