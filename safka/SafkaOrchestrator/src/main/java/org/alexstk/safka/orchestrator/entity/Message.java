package org.alexstk.safka.orchestrator.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Message implements Serializable {
    @NonNull
    private String content;
    private long timestamp;
}
