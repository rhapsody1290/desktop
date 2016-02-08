package me.ele.draenor.dao;

import me.ele.draenor.http.Response;

public interface OrderDao {

	Response make(String user_id, String cart_id);

	Response query(String user_id);

	Response queryAll();
}
