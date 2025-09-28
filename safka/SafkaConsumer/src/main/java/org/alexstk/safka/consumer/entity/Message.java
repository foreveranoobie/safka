package org.alexstk.safka.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Random;

@Getter
@ToString
@AllArgsConstructor
public class Message {
    private String key;
    @NonNull
    private String kafkaCommand;
    @NonNull
    private String topicName;
    private String contents;


}
