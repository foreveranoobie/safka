package org.alexstk.safka.orchestrator.io.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storozhuk.decoder.AuthService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Map;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.io.handler.impl.message.GetTopicsMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.message.ReadTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.CreateTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.PublishMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.proxy.MessageHandlerAuthProxy;

@ChannelHandler.Sharable
public class MessageInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final ObjectMapper objectMapper;
    private final Map<String, MessageHandler> topicHandlers;

    public MessageInboundHandlerAdapter(GetTopicsMessageHandler getTopicsMessageHandler,
        ReadTopicMessageHandler readTopicMessageHandler,
        PublishMessageHandler publishMessageHandler,
        CreateTopicMessageHandler createTopicMessageHandler, AuthService authService) {
        objectMapper = new ObjectMapper();
        topicHandlers = Map.of("GET_TOPICS",
            new MessageHandlerAuthProxy(getTopicsMessageHandler, authService), "READ",
            new MessageHandlerAuthProxy(readTopicMessageHandler, authService), "PUBLISH",
            new MessageHandlerAuthProxy(publishMessageHandler, authService), "CREATE_TOPIC",
            new MessageHandlerAuthProxy(createTopicMessageHandler, authService));
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            TcpRequestDto requestDto = objectMapper.readValue(msg.toString(), TcpRequestDto.class);
            topicHandlers.get(requestDto.getKafkaCommand()).readMessage(ctx, requestDto);
        } catch (Exception e) {
            System.err.println("Writing error");
            throw new RuntimeException(e);
        }
    }
}
