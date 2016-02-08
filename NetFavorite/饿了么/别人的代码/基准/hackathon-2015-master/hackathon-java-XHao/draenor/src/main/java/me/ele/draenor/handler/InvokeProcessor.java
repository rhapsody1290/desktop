package me.ele.draenor.handler;

import me.ele.draenor.http.Request;
import me.ele.draenor.http.Response;
import me.ele.draenor.http.Status;

public interface InvokeProcessor {

	InvokeProcessor Default = new InvokeProcessor() {
	};

	default Response process(Request request) {
		return new Response(Status.NOT_IMPLEMENTED, "");
	};

	default void addNext(InvokeProcessor processor) {
	};
}
