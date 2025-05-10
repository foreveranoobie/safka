package org.alexstk.safka.orchestrator.file;

import org.alexstk.safka.orchestrator.entity.Message;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FileProcessor {
    private static final String TOPICS_DIR = "E:\\git\\safka\\safka\\SafkaOrchestrator\\topics";

    public void createFolderForTopic(String topicName) throws IOException {
        try {
            Files.createDirectory(Path.of(TOPICS_DIR + "\\" + topicName));
        } catch (FileAlreadyExistsException ex) {
            System.err.printf("Topic %s already exists\n", topicName);
        }
    }

    public void writeMessageToTopic(String topicName, Message message) throws IOException {
        FileOutputStream fileOutputStream
                = new FileOutputStream(String.format("%s\\%s\\%d.txt", TOPICS_DIR, topicName, new Random().nextInt()));
        try (ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        }
    }

    public void cleanTopic(String topicName) throws IOException {
        Path directory = Paths.get(TOPICS_DIR , topicName);

        File directoryFile = directory.toFile();
        File[] files = directoryFile.listFiles();

        if (files != null) {
            for (File file : files) {
                Files.delete(file.toPath());
            }
        }
    }

    public List<String> listTopics(){
        return Arrays.stream(new File(TOPICS_DIR).listFiles()).map(File::getName).collect(Collectors.toList());
    }

    public List<Message> getMessagesFromTopic(String topicName){
        return Arrays.stream(new File(TOPICS_DIR + "\\" + topicName).listFiles()).map(file -> {
            try {
                return getMessageFromFile(file);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    private Message getMessageFromFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream
                = new FileInputStream(file);
        try(ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream)) {
            Message msg = (Message) objectInputStream.readObject();
            objectInputStream.close();
            return msg;
        }
    }
}
