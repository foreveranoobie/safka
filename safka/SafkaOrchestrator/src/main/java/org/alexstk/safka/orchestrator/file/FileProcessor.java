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
import org.alexstk.safka.orchestrator.entity.Message;

public class FileProcessor {

  private static final String TOPICS_DIR = "topics";

  public FileProcessor() {
    createTopicsFolder();
  }

  public void createFolderForTopic(String topicName) throws IOException {
    try {
      Files.createDirectory(Path.of(TOPICS_DIR + "\\" + topicName));
    } catch (FileAlreadyExistsException ex) {
      System.err.printf("Topic %s already exists\n", topicName);
    }
  }

  public void writeMessageToTopic(String topicName, Message message) throws IOException {
    if (topicExists(topicName)) {
      FileOutputStream fileOutputStream
          = new FileOutputStream(
          String.format("%s\\%s\\%s.txt", TOPICS_DIR, topicName, message.getKey()));
      try (ObjectOutputStream objectOutputStream
          = new ObjectOutputStream(fileOutputStream)) {
        objectOutputStream.writeObject(message);
        objectOutputStream.flush();
      }
    }
  }

  public void cleanTopic(String topicName) throws IOException {
    Path directory = Paths.get(TOPICS_DIR, topicName);

    File directoryFile = directory.toFile();
    File[] files = directoryFile.listFiles();

    if (files != null) {
      for (File file : files) {
        Files.delete(file.toPath());
      }
    }
  }

  public List<String> listTopics() {
    return Arrays.stream(new File(TOPICS_DIR).listFiles()).map(File::getName)
        .collect(Collectors.toList());
  }

  public List<Message> getMessagesFromTopic(String topicName) {
    if (topicExists(topicName)) {
      return Arrays.stream(new File(TOPICS_DIR + "\\" + topicName).listFiles()).map(file -> {
        try {
          return getMessageFromFile(file);
        } catch (IOException | ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      }).collect(Collectors.toList());
    } else {
      return List.of(new Message("Topic not found", 404, null));
    }
  }

  private Message getMessageFromFile(File file) throws IOException, ClassNotFoundException {
    FileInputStream fileInputStream
        = new FileInputStream(file);
    try (ObjectInputStream objectInputStream
        = new ObjectInputStream(fileInputStream)) {
      Message msg = (Message) objectInputStream.readObject();
      objectInputStream.close();
      return msg;
    }
  }

  public void createTopicsFolder() {
    try {
      Files.createDirectory(Path.of(TOPICS_DIR));
    } catch (FileAlreadyExistsException e) {
      System.out.println("Topics folder exists. No need to create");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void removeTopicsFolder() throws IOException {
    Path topicsDirPath = Path.of(TOPICS_DIR);
    if (Files.exists(topicsDirPath)) {
      listTopics().forEach(topicName -> {
        try {
          cleanTopic(topicName);
          Files.delete(Path.of(TOPICS_DIR + "\\" + topicName));
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
}
