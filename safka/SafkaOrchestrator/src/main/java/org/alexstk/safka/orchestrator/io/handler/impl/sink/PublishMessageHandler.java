package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import static org.alexstk.safka.orchestrator.io.handler.MessageHandlerHelper.isAdmin;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.alexstk.safka.orchestrator.entity.ResponseMessage;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.entity.request.RequestMessage;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class PublishMessageHandler extends AbstractSinkMessageHandler {

    public PublishMessageHandler(FileProcessor fileProcessor) {
        super(fileProcessor);
    }

    @Override
    public void performOperation(TcpRequestDto requestDto) throws Exception {
        String topicName = requestDto.getTopicName();
        List<String> roles = requestDto.getUserInfo().roles();
        if (isAdmin(requestDto.getUserInfo()) || (roles != null && roles.contains(topicName))) {
            fileProcessor.writeMessageToTopic(requestDto.getTopicName(),
                new RequestMessage(requestDto.getContents(), System.currentTimeMillis(),
                    requestDto.getKey(), null));
        }
    }
}
