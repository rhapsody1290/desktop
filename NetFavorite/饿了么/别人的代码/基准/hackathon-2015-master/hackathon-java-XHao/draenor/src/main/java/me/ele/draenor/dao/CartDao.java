package me.ele.draenor.dao;

import me.ele.draenor.http.Response;

public interface CartDao {

	Response create(String user_id);

	Response addFood(String user_id, String cart_id, String food_id, String count);

}
