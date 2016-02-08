package me.ele.draenor.util;

import java.util.HashMap;
import java.util.Map;

public class JsonReader {

	/**
	 * 只能分析<key, value> 简单的json
	 * 
	 * @return
	 */
	public static Map<String, String> parse(String content) {
		Map<String, String> params = new HashMap<>();

		// parse content,this is a json
		String key = null;
		String value = null;
		int mark = -1;
		boolean right = true;
		boolean start = false;
		boolean end = false;
		boolean parse = false;

		boolean isNum = false;
		boolean separator = false;

		for (int i = 0; i < content.length() && right; i++) {
			char c = content.charAt(i);
			switch (c) {
			case '{': {
				if (parse) {
				} else if (!start) {
					start = true;
				} else {
					right = false;
				}
				break;
			}
			case '}': {
				if (parse) {
					if (isNum) {
						int l = i - mark;
						value = l == 0 ? "" : content.substring(mark, i);
						params.put(key, value);
						key = null;
						value = null;
						mark = i;
						parse = false;
						isNum = false;
						separator = false;
					}
				} else if (!end) {
					end = true;
				} else {
					right = false;
				}
				break;
			}
			case ',': {
				if (isNum) {
					int l = i - mark;
					value = l == 0 ? "" : content.substring(mark, i);
					params.put(key, value);
					key = null;
					value = null;
					mark = i;
					parse = false;
					isNum = false;
					separator = false;
				} else if (parse) {
				}
				break;
			}
			case ':': {
				if (parse) {
				} else {
					separator = true;
				}
				break;
			}
			case '\n': {
				if (isNum) {
					int l = i - mark;
					value = l == 0 ? "" : content.substring(mark, i);
					params.put(key, value);
					key = null;
					value = null;
					mark = i;
					parse = false;
					isNum = false;
					separator = false;
				} else if (parse) {
					right = false;
				}
				break;
			}
			case ' ': {
				if (isNum) {
					int l = i - mark;
					value = l == 0 ? "" : content.substring(mark, i);
					params.put(key, value);
					key = null;
					value = null;
					mark = i;
					parse = false;
					isNum = false;
					separator = false;
				} else if (parse) {
				}
				break;
			}
			case '"': {
				if (isNum) {
					right = false;
				} else if (!parse) {
					mark = i;
					parse = true;
				} else {
					int l = i - mark;
					if (key != null) {
						value = l == 0 ? "" : content.substring(mark + 1, i);
						params.put(key, value);
						key = null;
					} else {
						key = l == 0 ? "" : content.substring(mark + 1, i);
					}
					value = null;
					mark = i;
					parse = false;
				}
				break;
			}
			default: {
				if (!parse && separator) {
					isNum = true;
					if ((int) c < 48 || (int) c > 57) {
						if ((int) c != 45){
							right = false;
						}else{
							parse = true;
							mark = i;
						}
					} else {
						parse = true;
						mark = i;
					}
				}
				break;
			}
			}
		}

		if (right) {
			return params;
		} else {
			return null;
		}
	}
}
