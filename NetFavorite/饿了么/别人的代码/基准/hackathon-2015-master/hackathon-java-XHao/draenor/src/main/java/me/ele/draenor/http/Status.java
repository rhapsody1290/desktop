package me.ele.draenor.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Status {

	public static Status OK = new Status("200 OK", HttpResponseStatus.OK);
	public static Status NO_CONTENT = new Status("204 No Content", HttpResponseStatus.NO_CONTENT);
	public static Status BAD_REQUEST = new Status("400 Bad Request", HttpResponseStatus.BAD_REQUEST);
	public static Status UNAUTHORIZED = new Status("401 Unauthorized", HttpResponseStatus.UNAUTHORIZED);
	public static Status FORBIDDEN = new Status("403 Forbidden", HttpResponseStatus.FORBIDDEN);
	public static Status NOT_FOUND = new Status("404 Not Found", HttpResponseStatus.NOT_FOUND);
	public static Status NOT_IMPLEMENTED = new Status("501 Not Implemented", HttpResponseStatus.NOT_IMPLEMENTED);

	private String val;
	@Getter
	private HttpResponseStatus status;

	public String toString() {
		return val;
	}
}
