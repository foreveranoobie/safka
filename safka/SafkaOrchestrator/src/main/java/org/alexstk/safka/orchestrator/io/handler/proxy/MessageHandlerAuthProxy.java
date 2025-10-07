package org.alexstk.safka.orchestrator.io.handler.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storozhuk.decoder.AuthService;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import java.util.List;
import org.alexstk.safka.orchestrator.entity.AccessToken;
import org.alexstk.safka.orchestrator.entity.ResponseMessage;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.io.handler.MessageHandler;

public class MessageHandlerAuthProxy implements MessageHandler {

    private MessageHandler delegate;
    private ObjectMapper objectMapper;
    private AuthService authService;

    public MessageHandlerAuthProxy(MessageHandler delegate, AuthService authService) {
        this.delegate = delegate;
        this.authService = authService;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void readMessage(ChannelHandlerContext ctx, TcpRequestDto requestDto)
        throws IOException {
        String accessToken = requestDto.getAccessToken().getAccessToken();
        if (authService.isTokenNotExpired(accessToken)) {
            requestDto.setUserInfo(authService.validateToken(accessToken));
            delegate.readMessage(ctx, requestDto);
        } else {
            try {
                ctx.writeAndFlush(List.of(new ResponseMessage("Topic not found", 404, null)));
                ctx.disconnect();
            } finally {
                ctx.disconnect();
            }
        }
    }
}
