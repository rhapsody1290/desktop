package me.ele.draenor.vo;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;

@Data
public class Food {
	private Integer id;
	private Integer price;
	private AtomicInteger stock = new AtomicInteger(0);

}