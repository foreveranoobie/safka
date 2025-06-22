package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexstk.safka.orchestrator.entity.Message;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;

@ExtendWith(MockitoExtension.class)
public class PublishMessageHandlerUnitTest {
    @Mock
    private FileProcessor fileProcessor;

    @InjectMocks
    private PublishMessageHandler handler;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldPublishMessage_whenPerformOperation_givenMessage() throws IOException {
        //given
        String topicName = "topicName";
        String key = "123";
        String contents = "contents";

        JsonNode givenMessage = objectMapper.createObjectNode().put("topicName", topicName).put("key", key).put("contents", contents);

        Message expectedMessage = new Message(contents, System.currentTimeMillis(), key);

        //when
        handler.performOperation(givenMessage);

        //then
        Mockito.verify(fileProcessor).writeMessageToTopic(eq(topicName), refEq(expectedMessage, "timestamp"));

    }
}