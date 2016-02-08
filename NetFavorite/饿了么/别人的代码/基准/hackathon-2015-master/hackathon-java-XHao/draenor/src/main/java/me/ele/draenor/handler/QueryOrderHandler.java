package me.ele.draenor.handler;

import me.ele.draenor.dao.DaoFactory;
import me.ele.draenor.dao.OrderDao;
import me.ele.draenor.dao.UserDao;
import me.ele.draenor.http.ExceptionResponse;
import me.ele.draenor.http.Request;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;

public class QueryOrderHandler implements InvokeProcessor {
	private static final String invalidToken = "无效的令牌";
	private static final String invalidTokenCode = "INVALID_ACCESS_TOKEN";

	private UserDao userDao = DaoFactory.getUserDaoInstance();
	private OrderDao orderDao = DaoFactory.getOrderDaoInstance();

	public Response process(Request request) {
		String access = request.getParams().get("access_token");
		String user_id = userDao.getUserID(access);

		if (user_id == null) {
			return new ExceptionResponse(Status.UNAUTHORIZED, invalidToken, invalidTokenCode);
		}

		Response response = orderDao.query(user_id);
		return response;
	}
}
