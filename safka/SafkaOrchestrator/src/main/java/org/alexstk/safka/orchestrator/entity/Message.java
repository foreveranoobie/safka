package org.alexstk.safka.orchestrator.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Message {
    @NonNull
    private String content;
    private long timestamp;
}
