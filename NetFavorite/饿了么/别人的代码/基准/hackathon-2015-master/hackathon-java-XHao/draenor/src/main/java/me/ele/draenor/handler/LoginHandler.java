package me.ele.draenor.handler;

import java.util.Map;

import me.ele.draenor.dao.DaoFactory;
import me.ele.draenor.dao.UserDao;
import me.ele.draenor.http.ExceptionResponse;
import me.ele.draenor.http.Request;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;
import me.ele.draenor.util.JsonReader;

public class LoginHandler implements InvokeProcessor {

	private final static String emptyRequest = "请求体为空";

	private static final String formatError = "格式错误";

	private static final String emptyRequestCode = "EMPTY_REQUEST";

	private static final String formatErrorCode = "MALFORMED_JSON";

	private UserDao dao = DaoFactory.getUserDaoInstance();

	@Override
	public Response process(Request request) {
		String content = request.getContent();
		if (content == null || content.length() == 0)
			return new ExceptionResponse(Status.BAD_REQUEST, emptyRequest, emptyRequestCode);

		Map<String, String> params = JsonReader.parse(content);

		if (params == null) {
			return new ExceptionResponse(Status.BAD_REQUEST, formatError, formatErrorCode);
		}

		String username = params.get("username");
		String password = params.get("password");

		if (username == null || password == null) {
			return new ExceptionResponse(Status.BAD_REQUEST, formatError, formatErrorCode);
		}

		return dao.login(username, password);
	}

}
