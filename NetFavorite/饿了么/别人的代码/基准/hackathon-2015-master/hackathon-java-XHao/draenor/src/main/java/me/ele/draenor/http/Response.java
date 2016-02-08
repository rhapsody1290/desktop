package me.ele.draenor.http;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Response {
	protected static final String ENTER = "\n";

	protected static final String version = "HTTP/1.1 ";
	protected static final String Common1 = "Content-Type: application/json; charset=UTF-8";
	protected static final String Common2 = "Content-Length: ";

	// protected static final String Common3 = "Connection: keep-alive";

	@Getter
	protected Status status;
	protected String content;

	public ByteBuffer wrapped() {
		StringBuilder sb = new StringBuilder(512);
		sb.append(version).append(status.toString()).append(ENTER).append(Common1).append(ENTER).append(Common2)
				.append(content.getBytes().length).append(ENTER).append(ENTER).append(content);

		byte[] bytes = sb.toString().getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		return buffer;
	}

	public ByteBuf buffer() {
		return Unpooled.wrappedBuffer(content.getBytes());
	}
}