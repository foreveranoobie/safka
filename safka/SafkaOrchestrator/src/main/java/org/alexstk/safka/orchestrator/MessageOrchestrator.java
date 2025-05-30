package org.alexstk.safka.orchestrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexstk.safka.orchestrator.entity.Message;
import org.alexstk.safka.orchestrator.entity.Topic;
import org.alexstk.safka.orchestrator.file.FileProcessor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageOrchestrator {
    private Map<String, Topic> topics = new ConcurrentHashMap<>(); // Thread-safe map
    private ExecutorService executorService = Executors.newFixedThreadPool(1); // Adjust thread pool size as needed
    private int tcpPort;
    private ObjectMapper objectMapper = new ObjectMapper();
    private FileProcessor fileProcessor = new FileProcessor();

    public MessageOrchestrator(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public boolean createTopic(String topicName) {
        if (topics.get(topicName) == null) {
            topics.put(topicName, new Topic(topicName));
            return true;
        }
        return false;
    }

    public void startListening() {
        try (ServerSocket serverSocket = new ServerSocket(tcpPort)) {
            System.out.println("MessageOrchestrator listening on port " + tcpPort);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClient(clientSocket)); // Handle each client in a separate thread
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions appropriately in a real application
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            String message;
            while ((message = reader.readLine()) != null) { // Read messages until client disconnects
                String response = processMessage(message); // Get the response to send back to the client
                if (response != null) {
                    writer.println(response); // Send the response
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processMessage(String message) throws JsonProcessingException {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String kafkaCommand = jsonNode.get("kafkaCommand").asText();

            if (kafkaCommand.equals("PUBLISH")) {
                String topicName = jsonNode.get("topicName").asText(); // Get topic name from JSON
                String contents = jsonNode.get("contents").asText();
                try {
                    fileProcessor.writeMessageToTopic(topicName, new Message(contents, System.currentTimeMillis()));
                    return "{\"status\": \"Message published for topic: " + topicName + "\"}";
                } catch (FileNotFoundException ex){
                    System.out.println("Topic not found: " + topicName);
                    return "404";
                }

                /*Topic topic = topics.get(topicName);
                if (topic != null) {
                    String contents = jsonNode.get("contents").asText();
                    topic.addMessage(new Message(contents, System.currentTimeMillis()));
                    System.out.println("Message published to topic " + topicName + ": " + contents);
                    return "{\"status\": \"Message published for topic: " + topicName + "\"}";
                } else {
                    System.out.println("Topic not found: " + topicName);
                    return "404";
                }*/
            } else if (kafkaCommand.equals("READ")) {
                String topicName = jsonNode.get("topicName").asText();
                try {
                    List<Message> messages = fileProcessor.getMessagesFromTopic(topicName);
                    return objectMapper.writeValueAsString(messages);
                } catch (RuntimeException ex) {
                    return "404";
                }
                /*String topicName = jsonNode.get("topicName").asText(); // Get topic name from JSON
                Topic topic = topics.get(topicName);
                if (topic != null) {
                    List<Message> messages = topic.getMessages();
                    return objectMapper.writeValueAsString(messages); // Return messages as JSON array
                } else {
                    return "{\"error\": \"Topic not found: " + topicName + "\"}"; // Return error as JSON
                }*/
            } else if (kafkaCommand.equals("CREATE_TOPIC")) {
                String topicName = jsonNode.get("topicName").asText(); // Get topic name from JSON
                //if(createTopic(topicName)) { // Create the topic
                fileProcessor.createFolderForTopic(topicName);
                //}
                return "{\"status\": \"Topic created: " + topicName + "\"}"; // Send success message
            } else if (kafkaCommand.equals("GET_TOPICS")) {
                return objectMapper.writeValueAsString(fileProcessor.listTopics());
            } else if (kafkaCommand.equals("CLEAN")) {
                String topicName = jsonNode.get("topicName").asText();
                fileProcessor.cleanTopic(topicName);
                return  "{\"status\": \"Topic removed\"}";
            }
            else {
                System.out.println("Unknown kafkaCommand: " + kafkaCommand);
            }
        } catch (IOException e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Missing field in JSON: " + e.getMessage()); // Handle missing fields
        }
        return null;
    }
}
