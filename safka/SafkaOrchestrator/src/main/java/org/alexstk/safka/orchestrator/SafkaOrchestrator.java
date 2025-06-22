package org.alexstk.safka.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.alexstk.safka.orchestrator.io.handler.impl.message.GetTopicsMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.message.ReadTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.CreateTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.PublishMessageHandler;

public class SafkaOrchestrator {
    public static void main(String[] args) {
        FileProcessor fileProcessor = new FileProcessor();
        ObjectMapper objectMapper = new ObjectMapper();
        GetTopicsMessageHandler getTopicsMessageHandler = new GetTopicsMessageHandler(objectMapper, fileProcessor);
        ReadTopicMessageHandler readTopicMessageHandler = new ReadTopicMessageHandler(objectMapper, fileProcessor);
        PublishMessageHandler publishMessageHandler = new PublishMessageHandler(fileProcessor);
        CreateTopicMessageHandler createTopicMessageHandler = new CreateTopicMessageHandler(fileProcessor);
        MessageOrchestrator orchestrator = new MessageOrchestrator(7500, getTopicsMessageHandler,
                readTopicMessageHandler, publishMessageHandler, createTopicMessageHandler);
        orchestrator.startListening();
    }
}
