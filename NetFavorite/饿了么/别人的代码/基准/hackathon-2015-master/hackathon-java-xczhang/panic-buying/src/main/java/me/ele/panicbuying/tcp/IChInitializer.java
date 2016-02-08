package me.ele.panicbuying.tcp;

import io.netty.channel.Channel;

/**
 * An initializer which can initialize each {@link Channel} once it was registered. </br>Be aware that the implementation must be thread-safe and can be
 * re-used.
 *
 * <pre>
 * public class MyChannelInitializer implements {@link IChInitializer} {
 *     public void initChannel({@link Channel} channel) {
 *         channel.pipeline().addLast(new MyHandlerA());
 *         channel.pipeline().addLast(new MyHandlerB());
 *     }
 * }
 * </pre>
 */
public interface IChInitializer {
    /**
     * This method will be called once the {@link Channel} was registered.
     *
     * @param ch
     *            the {@link Channel} which was registered.
     * @throws Exception
     *             thrown if an error occurs. In that case the {@link Channel} will be closed.
     */
    void initChannel(Channel ch) throws Exception;
}
