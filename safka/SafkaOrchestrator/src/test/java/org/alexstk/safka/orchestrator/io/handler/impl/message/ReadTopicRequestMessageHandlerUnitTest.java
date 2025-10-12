package org.alexstk.safka.orchestrator.io.handler.impl.message;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storozhuk.decoder.UserInfo;
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
        UserInfo userInfo = new UserInfo(null, List.of(topicName), null, null, null);
        TcpRequestDto givenRequestDto = new TcpRequestDto(null, topicName, null, null, userInfo,
            null);
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

    @Test
    public void shouldReturnMessage_whenPerformOperation_givenTopicNameAndAdminPermissions()
        throws JsonProcessingException {
        //given
        String topicName = "topicName";
        UserInfo userInfo = new UserInfo(null, null, List.of("ADMIN"), null, null);
        TcpRequestDto givenRequestDto = new TcpRequestDto(null, topicName, null, null, userInfo,
            null);
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

    @Test
    public void shouldReturnError_whenPerformOperation_givenTopicNameAbsentInRoles()
        throws JsonProcessingException {
        //given
        String topicName = "topicName";
        TcpRequestDto givenRequestDto = new TcpRequestDto(null, topicName, null, null,
            new UserInfo(null, null, null, null, null),
            null);
        String expectedMessage = objectMapper.writeValueAsString(
            new ResponseMessage("ERROR_UNAUTHORIZED", 0L, null));

        //when
        String actualMessage = handler.performOperation(givenRequestDto);

        //then
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
