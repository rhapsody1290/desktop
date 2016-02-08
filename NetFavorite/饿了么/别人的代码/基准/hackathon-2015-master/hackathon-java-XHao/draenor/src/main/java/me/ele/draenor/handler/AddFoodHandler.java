package me.ele.draenor.handler;

import java.util.Map;

import me.ele.draenor.dao.CartDao;
import me.ele.draenor.dao.DaoFactory;
import me.ele.draenor.dao.UserDao;
import me.ele.draenor.http.ExceptionResponse;
import me.ele.draenor.http.Request;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;
import me.ele.draenor.util.Cache;
import me.ele.draenor.util.JsonReader;

public class AddFoodHandler implements InvokeProcessor {

	private final static String emptyRequest = "请求体为空";
	private static final String emptyRequestCode = "EMPTY_REQUEST";
	private static final String formatError = "格式错误";
	private static final String formatErrorCode = "MALFORMED_JSON";
	private static final String invalidToken = "无效的令牌";
	private static final String invalidTokenCode = "INVALID_ACCESS_TOKEN";
	private static final String outOfLimit = "篮子中食物数量超过了三个";
	private static final String outOfLimitCode = "FOOD_OUT_OF_LIMIT";
	private static final String foodNotExist = "食物不存在";
	private static final String foodNotExistCode = "FOOD_NOT_FOUND";

	private UserDao userDao = DaoFactory.getUserDaoInstance();

	private CartDao cartDao = DaoFactory.getCartDaoInstance();

	public Response process(Request request) {

		String access = request.getParams().get("access_token");
		String user_id = userDao.getUserID(access);

		if (user_id == null) {
			return new ExceptionResponse(Status.UNAUTHORIZED, invalidToken, invalidTokenCode);
		}

		String content = request.getContent();
		if (content == null || content.length() == 0)
			return new ExceptionResponse(Status.BAD_REQUEST, emptyRequest, emptyRequestCode);

		Map<String, String> params = JsonReader.parse(content);

		if (params == null) {
			return new ExceptionResponse(Status.BAD_REQUEST, formatError, formatErrorCode);
		}

		String cart_id = parseCartId(request.getUrl());
		String food_id = params.get("food_id");
		String count = params.get("count");

		if (food_id == null || count == null) {
			return new ExceptionResponse(Status.BAD_REQUEST, formatError, formatErrorCode);
		}

		if (count != null && Integer.valueOf(count) > 3) {
			return new ExceptionResponse(Status.FORBIDDEN, outOfLimit, outOfLimitCode);
		}

		if (Cache.verifyFoodNotExist(Integer.valueOf(food_id))) {
			return new ExceptionResponse(Status.NOT_FOUND, foodNotExist, foodNotExistCode);
		}

		return cartDao.addFood(user_id, cart_id, food_id, count);

	}

	private String parseCartId(String path) {
		String cart_id = null;
		int start = path.indexOf('/', 1);
		if (start > -1) {
			cart_id = path.substring(start + 1);
		}
		return cart_id;
	}
}
