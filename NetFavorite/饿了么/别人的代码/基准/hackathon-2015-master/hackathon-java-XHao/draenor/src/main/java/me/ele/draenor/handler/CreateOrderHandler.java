package me.ele.draenor.handler;

import java.util.Map;

import me.ele.draenor.dao.DaoFactory;
import me.ele.draenor.dao.OrderDao;
import me.ele.draenor.dao.UserDao;
import me.ele.draenor.http.ExceptionResponse;
import me.ele.draenor.http.Request;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;
import me.ele.draenor.util.JsonReader;

public class CreateOrderHandler implements InvokeProcessor {

	private static final String emptyRequest = "请求体为空";
	private static final String emptyRequestCode = "EMPTY_REQUEST";
	private static final String formatError = "格式错误";
	private static final String formatErrorCode = "MALFORMED_JSON";
	private static final String invalidToken = "无效的令牌";
	private static final String invalidTokenCode = "INVALID_ACCESS_TOKEN";

	private OrderDao orderDao = DaoFactory.getOrderDaoInstance();
	private UserDao userDao = DaoFactory.getUserDaoInstance();

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

		String cart_id = params.get("cart_id");

		if (cart_id == null) {
			return new ExceptionResponse(Status.BAD_REQUEST, formatError, formatErrorCode);
		}

		return orderDao.make(user_id, cart_id);

	}

}
