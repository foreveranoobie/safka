package org.alexstk.safka.orchestrator.io.initializer;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HandlerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelInboundHandlerAdapter adapter;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast("encoder", new StringEncoder())
                .addLast("decoder", new StringDecoder())
                // Logging handler
                .addLast(new LoggingHandler())
                // Sends messages to the listener
                // Sends the response after any request
                .addLast(adapter);
    }
}
