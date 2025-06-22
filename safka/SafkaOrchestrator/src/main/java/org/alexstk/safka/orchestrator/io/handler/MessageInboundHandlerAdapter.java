package org.alexstk.safka.orchestrator.io.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.alexstk.safka.orchestrator.io.handler.impl.message.GetTopicsMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.message.ReadTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.CreateTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.PublishMessageHandler;

import java.util.Map;

@ChannelHandler.Sharable
public class MessageInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
    private final ObjectMapper objectMapper;
    private Map<String, MessageHandler> topicHandlers;

    public MessageInboundHandlerAdapter(GetTopicsMessageHandler getTopicsMessageHandler,
                                        ReadTopicMessageHandler readTopicMessageHandler, PublishMessageHandler publishMessageHandler,
                                        CreateTopicMessageHandler createTopicMessageHandler) {
        objectMapper = new ObjectMapper();
        topicHandlers = Map.of(
                "GET_TOPICS", getTopicsMessageHandler,
                "READ", readTopicMessageHandler,
                "PUBLISH", publishMessageHandler,
                "CREATE_TOPIC", createTopicMessageHandler
        );
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            JsonNode jsonNode = objectMapper.readTree(msg.toString());
            String kafkaCommand = jsonNode.get("kafkaCommand").asText();
            topicHandlers.get(kafkaCommand).readMessage(ctx, jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
