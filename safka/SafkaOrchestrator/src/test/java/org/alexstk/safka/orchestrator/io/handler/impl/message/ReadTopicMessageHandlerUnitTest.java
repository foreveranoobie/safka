package org.alexstk.safka.orchestrator.io.handler.impl.message;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.alexstk.safka.orchestrator.entity.Message;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReadTopicMessageHandlerUnitTest {

  @Mock
  private FileProcessor fileProcessor;

  private ObjectMapper objectMapper;
  private ReadTopicMessageHandler handler;

  @BeforeEach
  public void setUp() {
    objectMapper = new ObjectMapper();
    handler = new ReadTopicMessageHandler(objectMapper, fileProcessor);
  }

  @Test
  public void shouldReturnMessage_whenPerformOperation_givenTopicName()
      throws JsonProcessingException {
    //given
    String topicName = "topicName";
    JsonNode givenJsonMessage = objectMapper.createObjectNode()
        .put("topicName", topicName);
    Message topicMessage = new Message("message", System.currentTimeMillis(), "123");
    String expectedMessages = objectMapper.valueToTree(List.of(topicMessage)).toString();
    when(fileProcessor.getMessagesFromTopic(topicName)).thenReturn(List.of(topicMessage));

    //when
    String actualMessages = handler.performOperation(givenJsonMessage);

    //then
    Assertions.assertThat(actualMessages).isEqualTo(expectedMessages);
  }
}
