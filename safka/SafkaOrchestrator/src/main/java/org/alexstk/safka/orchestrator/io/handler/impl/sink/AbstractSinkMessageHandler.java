package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.alexstk.safka.orchestrator.io.handler.MessageHandler;

@AllArgsConstructor
public abstract class AbstractSinkMessageHandler implements MessageHandler {
    FileProcessor fileProcessor;

    public void readMessage(ChannelHandlerContext ctx, JsonNode jsonMessage){
        ctx.disconnect();
        performOperation(jsonMessage);
    }

    public abstract void performOperation(JsonNode jsonMessage);
}
