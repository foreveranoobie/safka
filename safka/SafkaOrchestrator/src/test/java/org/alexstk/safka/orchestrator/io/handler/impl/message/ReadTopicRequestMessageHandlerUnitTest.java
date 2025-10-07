package org.alexstk.safka.orchestrator.io.handler.impl.message;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.alexstk.safka.orchestrator.entity.ResponseMessage;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReadTopicRequestMessageHandlerUnitTest {

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
        TcpRequestDto givenRequestDto = new TcpRequestDto(null, topicName, null, null, null, null);
        ResponseMessage topicResponseMessage = new ResponseMessage("message",
            System.currentTimeMillis(), "123");
        String expectedMessages = objectMapper.valueToTree(List.of(topicResponseMessage))
            .toString();
        when(fileProcessor.getMessagesFromTopic(topicName)).thenReturn(
            List.of(topicResponseMessage));

        //when
        String actualMessages = handler.performOperation(givenRequestDto);

        //then
        Assertions.assertThat(actualMessages).isEqualTo(expectedMessages);
    }
}
