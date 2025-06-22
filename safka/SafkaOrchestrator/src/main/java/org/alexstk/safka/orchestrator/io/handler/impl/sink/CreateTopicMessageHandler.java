package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.databind.JsonNode;
import org.alexstk.safka.orchestrator.file.FileProcessor;

import java.io.IOException;

public class CreateTopicMessageHandler extends AbstractSinkMessageHandler {
    public CreateTopicMessageHandler(FileProcessor fileProcessor) {
        super(fileProcessor);
    }

    @Override
    public void performOperation(JsonNode jsonMessage) {
        String topicName = jsonMessage.get("topicName").asText();
        try {
            fileProcessor.createFolderForTopic(topicName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
