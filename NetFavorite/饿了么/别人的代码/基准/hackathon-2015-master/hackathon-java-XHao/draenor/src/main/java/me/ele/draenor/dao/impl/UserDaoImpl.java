package me.ele.draenor.dao.impl;

import me.ele.draenor.dao.UserDao;
import me.ele.draenor.http.ExceptionResponse;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;
import me.ele.draenor.util.Cache;
import me.ele.draenor.vo.User;

public class UserDaoImpl implements UserDao {
	private static final String errorPass = "用户名或密码错误";
	private static final String errorPassCode = "USER_AUTH_FAIL";

	public Response login(String username, String password) {
		if (username == null || password == null)
			return new ExceptionResponse(Status.FORBIDDEN, errorPass, errorPassCode);

		User user = Cache.getUser(username, password);

		if (user == null) {
			return new ExceptionResponse(Status.FORBIDDEN, errorPass, errorPassCode);
		}

		if (password.equals(user.getPassword())) {
			return new Response(Status.OK, user.toString());
		} else {
			return new ExceptionResponse(Status.FORBIDDEN, errorPass, errorPassCode);
		}

	}

	@Override
	public String getUserID(String access) {
		return Cache.getUserID(access);
	}

}
