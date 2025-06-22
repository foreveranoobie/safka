package org.alexstk.safka.orchestrator.io.handler.impl.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexstk.safka.orchestrator.entity.Message;
import org.alexstk.safka.orchestrator.file.FileProcessor;

import java.util.List;

public class ReadTopicMessageHandler extends AbstractResponseMessageHandler {
    public ReadTopicMessageHandler(ObjectMapper objectMapper, FileProcessor fileProcessor) {
        super(objectMapper, fileProcessor);
    }

    @Override
    public String performOperation(JsonNode jsonMessage) {
        String topicName = jsonMessage.get("topicName").asText();
        List<Message> messages = fileProcessor.getMessagesFromTopic(topicName);
        try {
            return objectMapper.writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
