package me.ele.draenor.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.ele.draenor.vo.Food;
import me.ele.draenor.vo.User;
import redis.clients.jedis.Jedis;

public class Cache {

	private static Map<String, String> token2user;
	private static Map<String, User> users;
	private static String rootToken;
	private static int[] foods_price;
	private static final ObjectMapper mapper = new ObjectMapper();
	private static AtomicInteger[] foods_stock;

	static final String Table_foodList = "food:list";
	static final String Table_foodDetail = "food:";
	static final String Table_foodStock = "food:stock";

	static {
		loadUsers();
		loadFoods();
		updateFoodStockJob();
	}

	private static void loadUsers() {
		token2user = new HashMap<>();
		users = new HashMap<>();
		Jedis jedis = JedisManager.getInstance().getJedis();
		List<String> ids = jedis.lrange("user:list", 0, -1);
		List<String> jsons = jedis.mget(ids.toArray(new String[0]));
		jsons.forEach(json -> {
			try {
				User u = mapper.readValue(json, User.class);
				if ("root".equals(u.getUsername()))
					rootToken = u.getAccess_token();
				users.put(u.getUsername(), u);
				token2user.put(u.getAccess_token(), u.getUser_id());
			} catch (Exception e) {
			}
		});

	}

	private static void loadFoods() {
		Jedis jedis = JedisManager.getInstance().getJedis();
		Set<String> ids = jedis.smembers(Table_foodList);
		int size = ids.size();
		foods_price = new int[size + 1];
		foods_stock = new AtomicInteger[size + 1];
		for (int i = 0; i < foods_stock.length; i++) {
			foods_stock[i] = new AtomicInteger(0);
		}

		ObjectMapper mapper = new ObjectMapper();
		if (size > 0) {
			String[] detail_ids = new String[ids.size()];
			int i = 0;
			for (String id : ids) {
				detail_ids[i++] = Table_foodDetail + id;
			}

			List<String> details = jedis.mget(detail_ids);

			details.forEach(val -> {
				try {
					Food food = mapper.readValue(val, Food.class);
					foods_price[food.getId()] = food.getPrice();
				} catch (Exception e) {
				}
			});

			updateAllStock(jedis);
		}
	}

	private static void updateAllStock(Jedis jedis) {
		Map<String, String> vals = jedis.hgetAll(Table_foodStock);
		vals.forEach((key, val) -> {
			foods_stock[Integer.valueOf(key)].getAndSet(Integer.valueOf(val));
		});
	}

	private static void updateFoodStockJob() {
		Jedis jedis = JedisManager.getInstance().getJedis();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						try {
							Thread.sleep(1000l);
						} catch (InterruptedException e) {
						}
						updateAllStock(jedis);
					}
				} finally {
					jedis.close();
				}
			}
		}).start();
	}

	public static User getUser(String username, String pass) {
		return users.get(username);
	}

	public static boolean verifyAdmin(String token) {
		return rootToken.equals(token);
	}

	public static String getUserID(String token) {
		return token2user.get(token);
	}

	public static void init() {
		// NOP
		// warm environment
	}

	public static String getFoodStock() {
		StringBuilder sb = new StringBuilder(3400);
		boolean flag = false;

		sb.append("[");
		for (int i = 1; i < foods_stock.length; i++) {
			int stock = foods_stock[i].get();
			if (stock > 0) {
				if (flag)
					sb.append(',');
				sb.append("{\"id\":").append(i).append(",\"price\":").append(foods_price[i]).append(",\"stock\":")
						.append(stock).append("}");
				flag = true;
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static int getPrice(int key) {
		return foods_price[key];
	}

	public static boolean verifyFoodNotExist(int food_id) {
		if (food_id < 0 || food_id >= foods_stock.length || foods_stock[food_id] == null) {
			return true;
		}
		return false;
	}

	public static String[] getUsers() {
		return token2user.values().toArray(new String[0]);
	}

	public static void updateFoodStock(String food_id, String stock) {
		foods_stock[Integer.valueOf(food_id)].addAndGet(0 - Integer.valueOf(stock));
	}

}
