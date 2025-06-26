package org.alexstk.safka.orchestrator.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class Topic {

  @NonNull
  private final String name;
  private final List<Message> messages;

  public Topic(String name) {
    this.name = name;
    this.messages = new ArrayList<>(); // Or ConcurrentLinkedQueue for better concurrency
  }

  public void addMessage(Message message) {
    this.messages.add(message);
  }
}
