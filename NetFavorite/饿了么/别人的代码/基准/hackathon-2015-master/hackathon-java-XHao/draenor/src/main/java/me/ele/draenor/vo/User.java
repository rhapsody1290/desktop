package me.ele.draenor.vo;

import lombok.Data;

@Data
public class User {
	private String user_id;
	private String username;
	private String password;
	private String access_token;

	private String val;

	public String toString() {
		if (val == null) {
			StringBuilder sb = new StringBuilder(128);
			sb.append("{\"user_id\":").append(user_id).append(",\"username\":\"").append(username)
					.append("\",\"access_token\":\"").append(access_token).append("\"}");
			val = sb.toString();
		}
		return val;
	}
}
