package org.alexstk.safka.orchestrator.io.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelHandlerContext;

public interface MessageHandler {
    void readMessage(ChannelHandlerContext ctx, JsonNode jsonMessage);
}
