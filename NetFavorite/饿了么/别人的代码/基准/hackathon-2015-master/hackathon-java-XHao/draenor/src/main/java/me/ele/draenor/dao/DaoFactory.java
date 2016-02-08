package me.ele.draenor.dao;

import me.ele.draenor.dao.impl.CartDaoImpl;
import me.ele.draenor.dao.impl.FoodDaoImpl;
import me.ele.draenor.dao.impl.OrderDaoImpl;
import me.ele.draenor.dao.impl.UserDaoImpl;

public class DaoFactory {

	private static UserDao userDao = new UserDaoImpl();
	private static FoodDao foodDao = new FoodDaoImpl();
	private static OrderDao orderDao = new OrderDaoImpl();
	private static CartDao cartDao = new CartDaoImpl();

	public static UserDao getUserDaoInstance() {
		return userDao;
	}

	public static FoodDao getFoodDaoInstance() {
		return foodDao;
	}

	public static OrderDao getOrderDaoInstance() {
		return orderDao;
	}

	public static CartDao getCartDaoInstance() {
		return cartDao;
	}
}
