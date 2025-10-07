package org.alexstk.safka.orchestrator.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.storozhuk.decoder.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.alexstk.safka.orchestrator.entity.AccessToken;

@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TcpRequestDto {

    private String kafkaCommand;
    private String topicName;
    private String contents;
    private String key;
    private UserInfo userInfo;
    private AccessToken accessToken;
}
