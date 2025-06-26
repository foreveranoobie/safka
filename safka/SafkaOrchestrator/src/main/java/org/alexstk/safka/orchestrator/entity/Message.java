package org.alexstk.safka.orchestrator.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Message implements Serializable {

  @NonNull
  private String content;
  private long timestamp;
  private String key;
}
