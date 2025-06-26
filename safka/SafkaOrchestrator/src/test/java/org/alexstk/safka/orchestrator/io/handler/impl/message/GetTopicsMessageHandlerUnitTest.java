package org.alexstk.safka.orchestrator.io.handler.impl.message;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetTopicsMessageHandlerUnitTest {

  @Mock
  private FileProcessor fileProcessor;

  private ObjectMapper objectMapper;
  private GetTopicsMessageHandler handler;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    handler = new GetTopicsMessageHandler(objectMapper, fileProcessor);
  }

  @Test
  public void shouldReturnListOfTopics_whenPerformOperation() throws JsonProcessingException {
    //given
    String firstTopic = "topic1";
    String secondTopic = "topic2";
    when(fileProcessor.listTopics()).thenReturn(List.of(firstTopic, secondTopic));

    String expectedResponse = objectMapper.valueToTree(List.of(firstTopic, secondTopic)).toString();

    //when
    String actualResponse = handler.performOperation(null);

    //then
    Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
  }
}
