package org.alexstk.safka.orchestrator.io.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.util.Map;
import org.alexstk.safka.orchestrator.entity.Message;
import org.alexstk.safka.orchestrator.io.handler.impl.message.GetTopicsMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.message.ReadTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.CreateTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.PublishMessageHandler;

@ChannelHandler.Sharable
public class MessageInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

  private final ObjectMapper objectMapper;
  private final Map<String, MessageHandler> topicHandlers;

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

  public void channelRead(ChannelHandlerContext ctx, Object msg) throws JsonProcessingException {
    try {
      JsonNode jsonNode = objectMapper.readTree(msg.toString());
      String kafkaCommand = jsonNode.get("kafkaCommand").asText();
      topicHandlers.get(kafkaCommand).readMessage(ctx, jsonNode);
    } catch (Exception e) {
      System.err.println("Writing error");
      throw new RuntimeException(e);
    }
  }
}
