package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class CreateTopicMessageHandlerUnitTest {
    @Mock
    private FileProcessor fileProcessor;
    @InjectMocks
    private CreateTopicMessageHandler handler;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldCreateTopic_whenPerformOperation_givenTopicName() throws IOException {
        //given
        String topicName = "topicName";

        JsonNode givenMessage = objectMapper.createObjectNode().put("topicName", topicName);

        //when
        handler.performOperation(givenMessage);

        //then
        org.mockito.Mockito.verify(fileProcessor).createFolderForTopic(topicName);

    }
}
