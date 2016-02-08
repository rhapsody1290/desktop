package me.ele.draenor.handler;

import me.ele.draenor.dao.DaoFactory;
import me.ele.draenor.dao.FoodDao;
import me.ele.draenor.dao.UserDao;
import me.ele.draenor.http.ExceptionResponse;
import me.ele.draenor.http.Request;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;

public class QueryFoodHandler implements InvokeProcessor {

	private static final String invalidToken = "无效的令牌";
	private static final String invalidTokenCode = "INVALID_ACCESS_TOKEN";

	private FoodDao foodDao = DaoFactory.getFoodDaoInstance();
	private UserDao userDao = DaoFactory.getUserDaoInstance();

	public Response process(Request request) {
		String access = request.getParams().get("access_token");

		String id = userDao.getUserID(access);
		if (id == null) {
			return new ExceptionResponse(Status.UNAUTHORIZED, invalidToken, invalidTokenCode);
		}
		return foodDao.queryFoodStock();
	}
}
