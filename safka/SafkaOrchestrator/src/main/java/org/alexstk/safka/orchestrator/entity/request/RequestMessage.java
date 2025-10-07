package org.alexstk.safka.orchestrator.entity.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.alexstk.safka.orchestrator.entity.AccessToken;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class RequestMessage implements Serializable {

  @NonNull
  private String content;
  private long timestamp;
  private String key;
  private AccessToken accessToken;
}
