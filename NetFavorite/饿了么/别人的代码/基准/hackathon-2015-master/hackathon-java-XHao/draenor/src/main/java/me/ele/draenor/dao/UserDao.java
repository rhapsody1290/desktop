package me.ele.draenor.dao;

import me.ele.draenor.http.Response;

public interface UserDao {

	public Response login(String username, String password);

	public String getUserID(String access);

}
