package org.alexstk.safka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.alexstk.safka.consumer.entity.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// ... (Message and Topic classes remain the same)

public class MessageConsumer { // Renamed to Client for more general use

    private final String orchestratorHost;
    private final int orchestratorPort;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Scanner scanner = new Scanner(System.in); // Scanner for user input

    public MessageConsumer(String orchestratorHost, int orchestratorPort) {
        this.orchestratorHost = orchestratorHost;
        this.orchestratorPort = orchestratorPort;
    }

    private String sendMessage(Message message) throws IOException {
        try (Socket socket = new Socket(orchestratorHost, orchestratorPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(objectMapper.writeValueAsString(message));
            return reader.readLine(); // Return the response
        }
    }

    public void startMenu() throws IOException {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create topic");
            System.out.println("2. Read topic messages");
            System.out.println("3. Publish message");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createTopic();
                    break;
                case 2:
                    readMessages();
                    break;
                case 3:
                    publishMessage();
                    break;
                case 4:
                    return; // Exit the menu
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void createTopic() throws IOException {
        System.out.print("Enter topic name: ");
        String topicName = scanner.nextLine();

        Message message = new Message("CREATE_TOPIC", topicName, null); //topicName will be passed as a field

        sendMessage(message);
    }

    private void readMessages() throws IOException {
        System.out.print("Enter topic name: ");
        String topicName = scanner.nextLine();

        Message readMessage = new Message("READ", topicName, null);

        String response = sendMessage(readMessage);
        List<Map> messages = parseMessagesResponse(response);

        if (messages != null) {
            System.out.println("Messages from topic " + topicName);
            messages.forEach(msg -> System.out.printf("Message: %s\nTimestamp: %s\n---\n", msg.get("content"), msg.get("timestamp")));
        }
    }

    private void publishMessage() throws IOException {
        System.out.print("Enter topic name: ");
        String topicName = scanner.nextLine();

        System.out.print("Enter message: ");
        String messageContent = scanner.nextLine();

        Message publishMessage = new Message("PUBLISH", topicName, messageContent);

        sendMessage(publishMessage);
    }

    private String parseResponse(String response) throws IOException {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.has("error")) {
                return jsonNode.get("error").asText();
            } else if (jsonNode.has("status")) {
                return jsonNode.get("status").asText();
            } else {
                return response; // some other response
            }
        } catch (IOException e) {
            throw new IOException("Error parsing response: " + e.getMessage());
        }
    }

    private List<Map> parseMessagesResponse(String response) throws IOException {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.has("error")) {
                System.out.println(jsonNode.get("error").asText());
                return null;
            } else {
                return objectMapper.convertValue(jsonNode, List.class);
            }
        } catch (IOException e) {
            throw new IOException("Error parsing response: " + e.getMessage());
        }
    }
}