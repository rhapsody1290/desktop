package me.ele.draenor.handler;

import me.ele.draenor.dao.DaoFactory;
import me.ele.draenor.dao.OrderDao;
import me.ele.draenor.http.ExceptionResponse;
import me.ele.draenor.http.Request;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;
import me.ele.draenor.util.Cache;

public class AdminQueryOrderHandler implements InvokeProcessor {
	private static final String invalidToken = "无效的令牌";
	private static final String invalidTokenCode = "INVALID_ACCESS_TOKEN";

	private OrderDao orderDao = DaoFactory.getOrderDaoInstance();

	public Response process(Request request) {
		String access = request.getParams().get("access_token");
		if (Cache.verifyAdmin(access)) {
			return orderDao.queryAll();
		} else {
			return new ExceptionResponse(Status.UNAUTHORIZED, invalidToken, invalidTokenCode);
		}
	}
}