package org.alexstk.safka.orchestrator.io.handler.impl.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class GetTopicsMessageHandler extends AbstractResponseMessageHandler {

    public GetTopicsMessageHandler(ObjectMapper objectMapper, FileProcessor fileProcessor) {
        super(objectMapper, fileProcessor);
    }

    @Override
    public String performOperation(JsonNode jsonMessage) throws JsonProcessingException {
        return objectMapper.writeValueAsString(fileProcessor.listTopics());
    }
}
