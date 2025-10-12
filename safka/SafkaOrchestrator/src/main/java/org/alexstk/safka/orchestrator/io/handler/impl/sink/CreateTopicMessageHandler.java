package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import static org.alexstk.safka.orchestrator.io.handler.MessageHandlerHelper.isAdmin;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class CreateTopicMessageHandler extends AbstractSinkMessageHandler {

    public CreateTopicMessageHandler(FileProcessor fileProcessor) {
        super(fileProcessor);
    }

    @Override
    public void performOperation(TcpRequestDto requestDto) throws Exception {
        if (isAdmin(requestDto.getUserInfo())) {
            fileProcessor.createFolderForTopic(requestDto.getTopicName());
        }
    }
}
