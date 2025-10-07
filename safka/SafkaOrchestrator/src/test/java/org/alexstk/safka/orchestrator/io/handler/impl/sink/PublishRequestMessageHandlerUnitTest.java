package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.never;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storozhuk.decoder.UserInfo;
import java.util.List;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.entity.request.RequestMessage;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PublishRequestMessageHandlerUnitTest {

    @Mock
    private FileProcessor fileProcessor;

    @InjectMocks
    private PublishMessageHandler handler;

    @Test
    public void shouldPublishMessage_whenPerformOperation_givenMessage() throws Exception {
        //given
        String topicName = "topicName";
        String key = "123";
        String contents = "contents";
        UserInfo userInfo = new UserInfo(null, List.of(topicName), null, null, null);
        TcpRequestDto requestDto = new TcpRequestDto(null, topicName, contents, key, userInfo,
            null);

        RequestMessage expectedRequestMessage = new RequestMessage(contents,
            System.currentTimeMillis(), key, null);

        //when
        handler.performOperation(requestDto);

        //then
        Mockito.verify(fileProcessor)
            .writeMessageToTopic(eq(topicName), refEq(expectedRequestMessage, "timestamp"));
    }

    @Test
    public void shouldNotPublishMessage_whenPerformOperation_givenTopicNameNotPresentInRoles()
        throws Exception {
        //given
        String topicName = "topicName";
        String key = "123";
        String contents = "contents";
        UserInfo userInfo = new UserInfo(null, List.of("topic1"), null, null, null);
        TcpRequestDto requestDto = new TcpRequestDto(null, topicName, contents, key, userInfo,
            null);

        RequestMessage expectedRequestMessage = new RequestMessage(contents,
            System.currentTimeMillis(), key, null);

        //when
        handler.performOperation(requestDto);

        //then
        Mockito.verify(fileProcessor, never())
            .writeMessageToTopic(any(), any());
    }
}