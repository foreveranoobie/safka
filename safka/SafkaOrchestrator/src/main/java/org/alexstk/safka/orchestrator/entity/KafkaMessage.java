package org.alexstk.safka.orchestrator.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class KafkaMessage {
    private String kafkaCommand;
    private String topic;
    private String contents;
}
