package org.alexstk.safka.orchestrator.io.handler.impl.message;

import static org.alexstk.safka.orchestrator.io.handler.MessageHandlerHelper.isAdmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storozhuk.decoder.UserInfo;
import java.util.List;
import org.alexstk.safka.orchestrator.entity.ResponseMessage;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class ReadTopicMessageHandler extends AbstractResponseMessageHandler {

    public ReadTopicMessageHandler(ObjectMapper objectMapper, FileProcessor fileProcessor) {
        super(objectMapper, fileProcessor);
    }

    @Override
    public String performOperation(TcpRequestDto requestDto) throws JsonProcessingException {
        String topicName = requestDto.getTopicName();
        UserInfo userInfo = requestDto.getUserInfo();
        if (isAdmin(userInfo)) {
            return objectMapper.writeValueAsString(fileProcessor.getMessagesFromTopic(topicName));
        }
        if (userInfo.roles() != null && userInfo.roles().contains(topicName)) {
            List<ResponseMessage> requestMessages = fileProcessor.getMessagesFromTopic(
                requestDto.getTopicName());
            return objectMapper.writeValueAsString(requestMessages);
        }
        return objectMapper.writeValueAsString(new ResponseMessage("ERROR_UNAUTHORIZED", 0L, null));
    }
}
