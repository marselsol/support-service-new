package com.example.utils;

import com.example.dto.PhraseOutput;
import lombok.RequiredArgsConstructor;
import org.example.inmemorybroker.MessageBroker;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageBrokerKafService {
    private final MessageBroker messageBroker;

    public PhraseOutput savePhrase(String topicName, String message) {
        messageBroker.sendMessage(topicName, message);
        return new PhraseOutput(message);
    }
}
