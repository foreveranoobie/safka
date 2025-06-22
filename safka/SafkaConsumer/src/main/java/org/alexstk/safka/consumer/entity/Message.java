package org.alexstk.safka.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Random;

@Getter
@AllArgsConstructor
@ToString
public class Message {
    @NonNull
    private String kafkaCommand;
    @NonNull
    private String topicName;
    private String contents;
    private String key;

    public Message(@NonNull String kafkaCommand, @NonNull String topicName, String contents) {
        this.kafkaCommand = kafkaCommand;
        this.topicName = topicName;
        this.contents = contents;
        key = String.valueOf(new Random().nextLong());
    }
}
