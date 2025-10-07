package org.alexstk.safka.orchestrator.io.handler.impl.sink;

import com.fasterxml.jackson.databind.JsonNode;
import org.alexstk.safka.orchestrator.entity.dto.TcpRequestDto;
import org.alexstk.safka.orchestrator.entity.request.RequestMessage;
import org.alexstk.safka.orchestrator.file.FileProcessor;

public class PublishMessageHandler extends AbstractSinkMessageHandler {

    public PublishMessageHandler(FileProcessor fileProcessor) {
        super(fileProcessor);
    }

    @Override
    public void performOperation(TcpRequestDto requestDto) throws Exception {
        fileProcessor.writeMessageToTopic(requestDto.getTopicName(),
            new RequestMessage(requestDto.getContents(), System.currentTimeMillis(),
                requestDto.getKey(), null));
    }
}
