package org.alexstk.safka.orchestrator.io.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.alexstk.safka.orchestrator.io.handler.impl.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SafkaOrchestratorEtETest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private FileProcessor fileProcessor;

  @BeforeEach
  public void setUp() {
    TestUtils.removeTopicsFolder();
    TestUtils.createTopicsFolder();
    fileProcessor = TestUtils.getFileProcessor();
  }

  @AfterEach
  public void cleanUp() {
    TestUtils.removeTopicsFolder();
  }

  @Test
  public void shouldCreateTopics() throws InterruptedException {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    CountDownLatch countDownLatch = new CountDownLatch(10);
    CountDownLatch lock = new CountDownLatch(10);
    for (int i = 0; i < 10; i++) {
      int finalI = i;
      executorService.schedule(() -> {
        try (Socket socket = new Socket("localhost", 7500);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {
          System.err.printf("Thread %d launched\n", finalI);
          String topicName = String.format("Topic-%d", finalI);
          JsonNode givenMessage = objectMapper.createObjectNode()
              .put("kafkaCommand", "CREATE_TOPIC").put("topicName", topicName);
          writer.println(givenMessage.toString());
          reader.readLine(); // Return the response
          lock.countDown();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        countDownLatch.countDown();
        System.err.printf("Thread %d finished\n", finalI);
      }, 250, TimeUnit.MILLISECONDS);
    }
    lock.await(1000, TimeUnit.MILLISECONDS);
    Assertions.assertThat(fileProcessor.listTopics())
        .containsExactlyInAnyOrder("Topic-0", "Topic-1", "Topic-2", "Topic-3", "Topic-4", "Topic-5",
            "Topic-6", "Topic-7", "Topic-8", "Topic-9");
  }
}
