package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.alexstk.safka.orchestrator.io.handler.MessageHandler;

@AllArgsConstructor
public abstract class AbstractSinkMessageHandler implements MessageHandler {

    FileProcessor fileProcessor;

    public void readMessage(ChannelHandlerContext ctx, TcpRequestDto requestDto) {
        ctx.disconnect();
        try {
            performOperation(requestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void performOperation(TcpRequestDto requestDto) throws IOException, Exception;
}
