package org.alexstk.safka.orchestrator.io.handler.impl.utils;

import java.io.File;
import java.io.IOException;
import lombok.Getter;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class TestUtils {

  @Getter
  private static final FileProcessor fileProcessor = new FileProcessor(String.format("..%stopics",
      File.separator));

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
