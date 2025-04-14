package org.alexstk.safka.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Message {
    @NonNull
    private String kafkaCommand;
    @NonNull
    private String topicName;
    private String contents;
}
