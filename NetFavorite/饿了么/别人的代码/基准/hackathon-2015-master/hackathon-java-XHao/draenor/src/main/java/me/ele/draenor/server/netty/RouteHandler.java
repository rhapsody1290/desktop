package me.ele.draenor.server.netty;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import me.ele.draenor.handler.AddFoodHandler;
import me.ele.draenor.handler.AdminQueryOrderHandler;
import me.ele.draenor.handler.CreateCartHandler;
import me.ele.draenor.handler.CreateOrderHandler;
import me.ele.draenor.handler.InvokeProcessor;
import me.ele.draenor.handler.LoginHandler;
import me.ele.draenor.handler.QueryFoodHandler;
import me.ele.draenor.handler.QueryOrderHandler;
import me.ele.draenor.http.Request;
import me.ele.draenor.http.Response;

@Sharable
public class RouteHandler extends ChannelHandlerAdapter {

	private static final Map<String, InvokeProcessor> getChain = new HashMap<>();
	private static final Map<String, InvokeProcessor> postChain = new HashMap<>();
	private static final Map<String, InvokeProcessor> patchChain = new HashMap<>();

	static {
		postChain.put("/login", new LoginHandler());
		getChain.put("/foods", new QueryFoodHandler());
		postChain.put("/carts", new CreateCartHandler());
		patchChain.put("/carts", new AddFoodHandler());
		postChain.put("/orders", new CreateOrderHandler());
		getChain.put("/orders", new QueryOrderHandler());
		getChain.put("/admin", new AdminQueryOrderHandler());
	}

	@SuppressWarnings("unused")
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		FullHttpRequest request = (FullHttpRequest) msg;
		try {
			Response httpResp = invoke(parse(request));

			DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
					httpResp.getStatus().getStatus(), httpResp.buffer());

			if (httpResp != null) {
				HttpHeaderUtil.setContentLength(response, response.content().readableBytes());
				HttpHeaderUtil.setKeepAlive(response, true);
				ctx.channel().writeAndFlush(response);
			} else {
				ctx.writeAndFlush(
						new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, httpResp.buffer()));
			}
		} finally {
			request.content().release();
		}
	}

	private Request parse(FullHttpRequest httpRequest) {
		Request request = new Request();

		request.setMethod(httpRequest.method().toString());

		String uri = httpRequest.uri();

		Map<String, String> params = new HashMap<>();

		int separate = uri.indexOf('?');

		if (separate == -1) {
			request.setUrl(uri);
		} else {
			request.setUrl(uri.substring(0, separate));
			uri = uri.substring(separate + 1);

			String key = null;
			String value = null;
			int mark = -1;
			for (int i = 0; i < uri.length(); i++) {
				char c = uri.charAt(i);
				switch (c) {
				case '&':
					int l = i - mark - 1;
					value = l == 0 ? "" : uri.substring(mark + 1, i);
					mark = i;
					if (key != null) {
						params.put(key, value);
					} else if (value != null && value.length() > 0) {
						params.put(value, "");
					}
					key = null;
					value = null;
					break;
				case '=':
					if (key != null)
						break;
					key = uri.substring(mark + 1, i);
					mark = i;
					break;
				}
			}
			if (key != null) {
				int l = uri.length() - mark - 1;
				value = l == 0 ? "" : uri.substring(mark + 1);
				params.put(key, value);
			} else if (mark < uri.length()) {
				key = uri.substring(mark + 1);
				if (key != null && key.length() > 0)
					params.put(key, "");
			}
		}

		params.putIfAbsent("access_token", (String) httpRequest.headers().get("Access-Token"));

		request.setParams(params);
		request.setContent(httpRequest.content().toString(CharsetUtil.UTF_8));
		return request;
	}

	private Response invoke(Request request) {
		Map<String, InvokeProcessor> chain = null;
		switch (request.getMethod()) {
		case "GET":
			chain = getChain;
			break;
		case "POST":
			chain = postChain;
			break;
		case "PATCH":
			chain = patchChain;
			break;
		}
		return getInvoker(request.getUrl(), chain).process(request);
	}

	private InvokeProcessor getInvoker(String path, Map<String, InvokeProcessor> chain) {
		if (chain == null)
			return InvokeProcessor.Default;
		if (chain.containsKey(path)) {
			return chain.get(path);
		} else {
			int index = path.lastIndexOf('/');
			if (index == 0) {
				return InvokeProcessor.Default;
			} else {
				return getInvoker(path.substring(0, index), chain);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
