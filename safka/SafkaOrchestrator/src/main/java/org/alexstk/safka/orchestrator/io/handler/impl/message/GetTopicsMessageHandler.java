package org.alexstk.safka.orchestrator.io.handler.impl.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class GetTopicsMessageHandler extends AbstractResponseMessageHandler {

    public GetTopicsMessageHandler(ObjectMapper objectMapper, FileProcessor fileProcessor) {
        super(objectMapper, fileProcessor);
    }

    @Override
    public String performOperation(TcpRequestDto requestDto) throws JsonProcessingException {
        List<String> existingTopics = fileProcessor.listTopics();
        List<String> roles = requestDto.getUserInfo().roles();
        if (roles != null) {
            List<String> authorizedTopics = existingTopics.stream().filter(roles::contains)
                .toList();
            return objectMapper.writeValueAsString(authorizedTopics);
        }
        return objectMapper.writeValueAsString(List.of());
    }
}
