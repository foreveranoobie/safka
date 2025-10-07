package org.alexstk.safka.orchestrator.io.handler.impl.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.alexstk.safka.orchestrator.entity.ResponseMessage;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class ReadTopicMessageHandler extends AbstractResponseMessageHandler {

    public ReadTopicMessageHandler(ObjectMapper objectMapper, FileProcessor fileProcessor) {
        super(objectMapper, fileProcessor);
    }

    @Override
    public String performOperation(TcpRequestDto requestDto) throws JsonProcessingException {
        List<ResponseMessage> requestMessages = fileProcessor.getMessagesFromTopic(
            requestDto.getTopicName());
        return objectMapper.writeValueAsString(requestMessages);
    }
}
