package org.alexstk.safka.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storozhuk.decoder.AuthService;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.alexstk.safka.orchestrator.io.handler.impl.message.GetTopicsMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.message.ReadTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.CreateTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.PublishMessageHandler;

public class SafkaOrchestrator {

    public static void main(String[] args) {
        int port = getPort(args);
        String topic = getTopicsDir(args);
        FileProcessor fileProcessor = new FileProcessor(topic);
        ObjectMapper objectMapper = new ObjectMapper();
        AuthService authService = new AuthService(System.getenv("ISSUER_BASE_URL"));
        MessageOrchestrator orchestrator = getMessageOrchestrator(objectMapper, fileProcessor, port,
            authService);
        orchestrator.startListening();
    }

    private static MessageOrchestrator getMessageOrchestrator(ObjectMapper objectMapper,
        FileProcessor fileProcessor, int port,
        AuthService authService) {
        GetTopicsMessageHandler getTopicsMessageHandler = new GetTopicsMessageHandler(objectMapper,
            fileProcessor);
        ReadTopicMessageHandler readTopicMessageHandler = new ReadTopicMessageHandler(objectMapper,
            fileProcessor);
        PublishMessageHandler publishMessageHandler = new PublishMessageHandler(fileProcessor);
        CreateTopicMessageHandler createTopicMessageHandler = new CreateTopicMessageHandler(
            fileProcessor);
        return new MessageOrchestrator(port, getTopicsMessageHandler,
            readTopicMessageHandler, publishMessageHandler, createTopicMessageHandler, authService);
    }

    private static int getPort(String[] args) {
        if (args == null || args.length == 0 || !args[0].matches("[0-9]+")) {
            System.out.println("Port is not provided. Using default 7500 instead");
            return 7500;
        } else {
            int port = Integer.parseInt(args[0]);
            System.out.printf("Starting application with port assigned to %d\n", port);
            return port;
        }
    }

    private static String getTopicsDir(String[] args) {
        if (args == null || args.length < 2) {
            System.out.println("Topics path is not provided. Using default 'topics' instead");
            return "topics";
        } else {
            String topic = args[1];
            System.out.printf("Starting application with topics folder assigned to %s\n", topic);
            return topic;
        }
    }
}
