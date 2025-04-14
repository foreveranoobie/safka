package org.alexstk.safka.orchestrator.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Topic {
    @NonNull
    private String name;
    private List<Message> messages;

    public Topic(String name) {
        this.name = name;
        this.messages = new ArrayList<>(); // Or ConcurrentLinkedQueue for better concurrency
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
