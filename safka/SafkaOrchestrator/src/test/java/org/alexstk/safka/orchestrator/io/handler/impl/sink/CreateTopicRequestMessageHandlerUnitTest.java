package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateTopicRequestMessageHandlerUnitTest {

  @Mock
  private FileProcessor fileProcessor;
  @InjectMocks
  private CreateTopicMessageHandler handler;

  @Test
  public void shouldCreateTopic_whenPerformOperation_givenTopicName() throws Exception {
    //given
    String topicName = "topicName";
    TcpRequestDto requestDto = new TcpRequestDto(null, topicName, null, null, null, null);

    //when
    handler.performOperation(requestDto);

    //then
    org.mockito.Mockito.verify(fileProcessor).createFolderForTopic(topicName);

  }
}
