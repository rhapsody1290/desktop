package me.ele.draenor.dao.impl;

import me.ele.draenor.dao.FoodDao;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;
import me.ele.draenor.util.Cache;

public class FoodDaoImpl implements FoodDao {

	@Override
	public Response queryFoodStock() {
		String content = Cache.getFoodStock();
		return new Response(Status.OK, content);
	}

}
