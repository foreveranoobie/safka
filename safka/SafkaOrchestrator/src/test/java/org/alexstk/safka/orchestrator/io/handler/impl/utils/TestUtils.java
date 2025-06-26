package org.alexstk.safka.orchestrator.io.handler.impl.utils;

import java.io.IOException;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class TestUtils {

  private static final FileProcessor fileProcessor = new FileProcessor();

  public static void removeTopicsFolder() {
    try {
      fileProcessor.removeTopicsFolder();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void createTopicsFolder() {
    fileProcessor.createTopicsFolder();
  }
}
