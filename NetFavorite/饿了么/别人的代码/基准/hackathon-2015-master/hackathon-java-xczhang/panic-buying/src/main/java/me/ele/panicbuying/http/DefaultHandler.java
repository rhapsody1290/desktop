package me.ele.panicbuying.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;

import java.util.List;
import java.util.Map;

public class DefaultHandler extends OneResponseHandler {
    private final HttpResponseStatus defaultStatus;

    public DefaultHandler(HttpResponseStatus defaultStatus) {
        if (defaultStatus == null) {
            throw new NullPointerException("Default status should not be null!");
        }
        this.defaultStatus = defaultStatus;
    }

    @Override
    public FullHttpResponse handle(HttpRequest httpRequest, Map<String, List<String>> params, ByteBuf byteBuf) {
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, defaultStatus);
        httpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, 0);
        return httpResponse;
    }
}
