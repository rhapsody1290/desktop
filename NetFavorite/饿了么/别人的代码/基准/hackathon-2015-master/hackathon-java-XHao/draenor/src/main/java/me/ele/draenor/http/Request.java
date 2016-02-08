package me.ele.draenor.http;

import java.util.Map;

import lombok.Data;

@Data
public class Request {

	private String method;
	private String url;
	private Map<String, String> params;
	private String content;

	private boolean isOk;
}
