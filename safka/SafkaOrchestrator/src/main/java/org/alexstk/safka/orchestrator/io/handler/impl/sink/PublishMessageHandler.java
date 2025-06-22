package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.databind.JsonNode;
import org.alexstk.safka.orchestrator.entity.Message;
import org.alexstk.safka.orchestrator.file.FileProcessor;

import java.io.IOException;

public class PublishMessageHandler extends AbstractSinkMessageHandler {
    public PublishMessageHandler(FileProcessor fileProcessor) {
        super(fileProcessor);
    }

    @Override
    public void performOperation(JsonNode jsonMessage) {
        String topicName = jsonMessage.get("topicName").asText(); // Get topic name from JSON
        String contents = jsonMessage.get("contents").asText();
        String key = jsonMessage.get("key").asText();
        try {
            fileProcessor.writeMessageToTopic(topicName, new Message(contents, System.currentTimeMillis(), key));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
