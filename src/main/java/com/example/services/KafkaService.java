package com.example.services;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;


public interface KafkaService {
    PhraseOutput saveInputPhrase(String topicName, PhraseInput message);

    void phrasesMethodListener(PhraseInput message);
}