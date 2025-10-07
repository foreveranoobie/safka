package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    TcpRequestDto requestDto = new TcpRequestDto(null, topicName, contents, key, null, null);

    RequestMessage expectedRequestMessage = new RequestMessage(contents, System.currentTimeMillis(), key, null);

    //when
    handler.performOperation(requestDto);

    //then
    Mockito.verify(fileProcessor)
        .writeMessageToTopic(eq(topicName), refEq(expectedRequestMessage, "timestamp"));

  }
}