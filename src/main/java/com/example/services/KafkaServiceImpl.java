package com.example.services;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import com.example.repository.PhraseStorageImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "features.kafka-sync", havingValue = "true")
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PhraseStorageImpl phraseStorage;

    public PhraseOutput saveInputPhrase(String topicName, PhraseInput message) {
        kafkaTemplate.send(topicName, message);
        return new PhraseOutput(message.phrase());
    }

    @KafkaListener(topics = "addPhrases", groupId = "support-service-group")
    public void phrasesMethodListener(PhraseInput message) {
        phraseStorage.addPhrase(message.phrase());
        log.info("Read the message and added it to phraseStorage: {}", message);
    }
}