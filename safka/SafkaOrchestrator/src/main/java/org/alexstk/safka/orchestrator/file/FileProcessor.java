package org.alexstk.safka.orchestrator.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.alexstk.safka.orchestrator.entity.request.RequestMessage;
import org.alexstk.safka.orchestrator.entity.ResponseMessage;

public class FileProcessor {

    private String topicsDir;

    public FileProcessor(String topicsDir) {
        if (topicsDir == null) {
            this.topicsDir = "topics";
        } else {
            this.topicsDir = topicsDir;
        }
        createTopicsFolder();
    }

    public void createFolderForTopic(String topicName) throws IOException {
        try {
            Files.createDirectory(Path.of(getTopicPathWithFileSeparator(topicName)));
        } catch (FileAlreadyExistsException ex) {
            System.err.printf("Topic %s already exists\n", topicName);
        }
    }

    public void writeMessageToTopic(String topicName, RequestMessage requestMessage) throws IOException {
        if (topicExists(topicName)) {
            String pathTxt = String.format("%s.txt",
                getPathSeparatedWithArguments(getTopicPathWithFileSeparator(topicName),
                    String.format("%s-%s", requestMessage.getKey(), requestMessage.getTimestamp())));
            System.err.printf("Writing message: %s\n", pathTxt);
            FileOutputStream fileOutputStream
                = new FileOutputStream(pathTxt);
            try (ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(requestMessage);
                objectOutputStream.flush();
            }
        }
    }

    public void cleanTopic(String topicName) throws IOException {
        Path directory = Paths.get(topicsDir, topicName);

        File directoryFile = directory.toFile();
        File[] files = directoryFile.listFiles();

        if (files != null) {
            for (File file : files) {
                Files.delete(file.toPath());
            }
        }
    }

    public List<String> listTopics() {
        return Arrays.stream(new File(topicsDir).listFiles()).map(File::getName)
            .collect(Collectors.toList());
    }

    public List<ResponseMessage> getMessagesFromTopic(String topicName) {
        if (topicExists(topicName)) {
            return Arrays.stream(new File(getTopicPathWithFileSeparator(topicName)).listFiles())
                .map(file -> {
                    try {
                        return getMessageFromFile(file);
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
        } else {
            return List.of(new ResponseMessage("Topic not found", 404, null));
        }
    }

    private ResponseMessage getMessageFromFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream
            = new FileInputStream(file);
        try (ObjectInputStream objectInputStream
            = new ObjectInputStream(fileInputStream)) {
            ResponseMessage msg = (ResponseMessage) objectInputStream.readObject();
            objectInputStream.close();
            return msg;
        }
    }

    public void createTopicsFolder() {
        try {
            Files.createDirectory(Path.of(topicsDir));
        } catch (FileAlreadyExistsException e) {
            System.out.println("Topics folder exists. No need to create");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeTopicsFolder() throws IOException {
        Path topicsDirPath = Path.of(topicsDir);
        if (Files.exists(topicsDirPath)) {
            listTopics().forEach(topicName -> {
                try {
                    cleanTopic(topicName);
                    Files.delete(Path.of(getTopicPathWithFileSeparator(topicName)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Files.delete(topicsDirPath);
        }
    }

    public boolean topicExists(String topicName) {
        return listTopics().stream().anyMatch(topicName::equals);
    }

    private String getPathSeparatedWithArguments(String leftSubPath, String rightSubPath) {
        return String.format("%s%s%s", leftSubPath, File.separator, rightSubPath);
    }

    private String getTopicPathWithFileSeparator(String topicName) {
        return String.format("%s%s%s", topicsDir, File.separator, topicName);
    }
}
