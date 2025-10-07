package org.alexstk.safka.orchestrator.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.alexstk.safka.orchestrator.entity.request.RequestMessage;

@Getter
@ToString
public class Topic {

  @NonNull
  private final String name;
  private final List<RequestMessage> requestMessages;

  public Topic(String name) {
    this.name = name;
    this.requestMessages = new ArrayList<>(); // Or ConcurrentLinkedQueue for better concurrency
  }

  public void addMessage(RequestMessage requestMessage) {
    this.requestMessages.add(requestMessage);
  }
}
