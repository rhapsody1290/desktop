package com.model;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Cart {
	// 全局参数
	// 购物车ID
	public static int cartCount = 0;
	//购物车列表(购物车名字，购物车对象)
	public static Map<String, Cart> carts = new HashMap<String, Cart>();
	
	public String owner;
	public String cartName;
	public Map<Integer, Integer> foods;
	public int FoodSum;
	
	private Cart(String owner){
		this.owner = owner;
		cartName = String.valueOf(Cart.cartCount++);
		foods = new HashMap<Integer, Integer>();
		FoodSum = 0;
	}
	public static String getCartName(String owner){
		Cart cart = new Cart(owner);
		Cart.carts.put(cart.cartName, cart);
		return cart.cartName;
	}

}
