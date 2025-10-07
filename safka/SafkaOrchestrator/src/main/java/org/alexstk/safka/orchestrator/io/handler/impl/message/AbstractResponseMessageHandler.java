package org.alexstk.safka.orchestrator.io.handler.impl.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import org.alexstk.safka.orchestrator.entity.ResponseMessage;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.alexstk.safka.orchestrator.io.handler.MessageHandler;

@AllArgsConstructor
public abstract class AbstractResponseMessageHandler implements MessageHandler {

    ObjectMapper objectMapper;
    FileProcessor fileProcessor;

    @Override
    public void readMessage(ChannelHandlerContext ctx, TcpRequestDto requestDto) {
        try {
            performOperation(requestDto);
            String response = performOperation(requestDto);
            ctx.writeAndFlush(response);
            ctx.disconnect();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            try {
                String response = objectMapper.writeValueAsString(new ResponseMessage("ERROR", 0L, null));
                ctx.writeAndFlush(response);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            ctx.disconnect();
        }
    }

    public abstract String performOperation(TcpRequestDto requestDto) throws JsonProcessingException;
}
