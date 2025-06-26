package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateTopicMessageHandlerUnitTest {

  @Mock
  private FileProcessor fileProcessor;
  @InjectMocks
  private CreateTopicMessageHandler handler;

  private final ObjectMapper objectMapper = new ObjectMapper();

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
