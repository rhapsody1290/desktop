package me.ele.draenor.http;

public class ExceptionResponse extends Response {

	public ExceptionResponse(Status status, String content, String code) {
		super(status, content);
		StringBuilder sb = new StringBuilder(64);
		sb.append("{\"message\":\"").append(content).append("\",").append("\"code\":\"").append(code).append("\"}");
		this.content = sb.toString();
	}
}
