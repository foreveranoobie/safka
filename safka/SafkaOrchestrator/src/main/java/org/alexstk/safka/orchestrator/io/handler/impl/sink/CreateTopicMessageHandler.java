package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class CreateTopicMessageHandler extends AbstractSinkMessageHandler {

  public CreateTopicMessageHandler(FileProcessor fileProcessor) {
    super(fileProcessor);
  }

  @Override
  public void performOperation(JsonNode jsonMessage) throws IOException {
    String topicName = jsonMessage.get("topicName").asText();
    fileProcessor.createFolderForTopic(topicName);
  }
}
