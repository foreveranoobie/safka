package org.alexstk.safka.orchestrator.file;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.alexstk.safka.orchestrator.entity.request.RequestMessage;
import org.alexstk.safka.orchestrator.entity.ResponseMessage;
import org.alexstk.safka.orchestrator.io.handler.impl.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileProcessorUnitTest {

  private static final String TOPICS_DIR = String.format("..%stopics", File.separator);
  private FileProcessor fileProcessor = TestUtils.getFileProcessor();

  @BeforeEach
  public void setUp(){
    TestUtils.createTopicsFolder();
  }

  @AfterEach
  public void cleanUp() throws IOException {
    TestUtils.removeTopicsFolder();
  }

  @Test
  public void shouldCreateFolderForTopic_whenCreateFolderForTopic_givenTopicName()
      throws IOException {
    //given
    String givenTopicName = "test-topic";

    //when
    fileProcessor.createFolderForTopic(givenTopicName);

    //then
    Assertions.assertThat(Arrays.stream(new File(TOPICS_DIR).listFiles()).map(File::getName)
        .filter(givenTopicName::equals).findAny().isPresent()).isTrue();
  }

  @Test
  public void shouldCreateTopicOnlyOnce_whenCreateFolderForTopic_givenTopicNameWith2TimesCalled()
      throws IOException {
    //given
    String givenTopicName = "test-topic";
    fileProcessor.createFolderForTopic(givenTopicName);

    //when
    fileProcessor.createFolderForTopic(givenTopicName);

    //then
    Assertions.assertThat(Arrays.stream(new File(TOPICS_DIR).listFiles())).hasSize(1);
  }

  @Test
  public void shouldReturnEmptyList_whenListTopics_givenEmptyTopicsDirectory() {
    //given
    //when
    List<String> actualTopics = fileProcessor.listTopics();

    //then
    Assertions.assertThat(actualTopics).isEmpty();
  }

  @Test
  public void shouldReturnListOfTopics_whenListTopics_givenTopicsDirectoryWith2Topics()
      throws IOException {
    //given
    String firstTopicName = "test-topic1";
    String secondTopicName = "test-topic2";

    fileProcessor.createFolderForTopic(firstTopicName);
    fileProcessor.createFolderForTopic(secondTopicName);

    //when
    List<String> actualTopics = fileProcessor.listTopics();

    //then
    Assertions.assertThat(actualTopics).containsExactlyInAnyOrder(firstTopicName, secondTopicName);
  }

  @Test
  public void shouldWriteMessageToTheTopic_whenWriteMessageToTopic_givenMessageAndTopicName()
      throws IOException {
    //given
    RequestMessage givenRequestMessage = new RequestMessage("Test", System.currentTimeMillis(), "123", null);
    ResponseMessage expectedResponseMessage = new ResponseMessage("Test", System.currentTimeMillis(), "123");
    String givenTopicName = "test-topic";
    fileProcessor.createFolderForTopic(givenTopicName);

    //when
    fileProcessor.writeMessageToTopic(givenTopicName, givenRequestMessage);

    //then
    List<ResponseMessage> actualRequestMessages = fileProcessor.getMessagesFromTopic(givenTopicName);
    Assertions.assertThat(actualRequestMessages).usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrder(expectedResponseMessage);
  }

  @Test
  public void shouldNotWriteMessageToTheTopic_whenWriteMessageToTopic_givenMessageAndAbsentTopicName()
      throws IOException {
    //given
    RequestMessage givenRequestMessage = new RequestMessage("Test", System.currentTimeMillis(), "123", null);
    String givenTopicName = "test-topic";

    ResponseMessage expectedResponseMessage = new ResponseMessage("Topic not found", 404, "123");

    //when
    fileProcessor.writeMessageToTopic(givenTopicName, givenRequestMessage);

    //then
    List<ResponseMessage> actualRequestMessages = fileProcessor.getMessagesFromTopic(givenTopicName);
    Assertions.assertThat(actualRequestMessages).usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrder(expectedResponseMessage);
  }
}
