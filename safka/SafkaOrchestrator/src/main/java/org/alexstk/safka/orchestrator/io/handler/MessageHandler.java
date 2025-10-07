package org.alexstk.safka.orchestrator.io.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;

public interface MessageHandler {

  void readMessage(ChannelHandlerContext ctx, TcpRequestDto requestDto) throws IOException;
}
