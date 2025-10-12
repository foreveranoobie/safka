package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storozhuk.decoder.UserInfo;
import java.io.IOException;
import java.util.List;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateTopicRequestMessageHandlerUnitTest {

    @Mock
    private FileProcessor fileProcessor;
    @InjectMocks
    private CreateTopicMessageHandler handler;

    @Test
    public void shouldCreateTopic_whenPerformOperation_givenTopicNameAndAdminPermissions()
        throws Exception {
        //given
        String topicName = "topicName";
        UserInfo userInfo = new UserInfo(null, null, List.of("ADMIN"), null, null);
        TcpRequestDto requestDto = new TcpRequestDto(null, topicName, null, null, userInfo, null);

        //when
        handler.performOperation(requestDto);

        //then
        verify(fileProcessor).createFolderForTopic(topicName);
    }

    @Test
    public void shouldNotCreateTopic_whenPerformOperation_givenTopicNameAndNoAdminPermissions()
        throws Exception {
        //given
        String topicName = "topicName";
        UserInfo userInfo = new UserInfo(null, null, null, null, null);
        TcpRequestDto requestDto = new TcpRequestDto(null, topicName, null, null, userInfo, null);

        //when
        handler.performOperation(requestDto);

        //then
        verify(fileProcessor, never()).createFolderForTopic(any());
    }
}
