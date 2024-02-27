package com.example.services;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import com.example.repository.PhraseStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PhraseServiceImpl implements PhraseService {
    @Autowired
    private PhraseStorage phraseStorage;
    @Autowired(required = false)
    private PhraseKafkaManagerImpl kafkaService;

    @Value("${features.kafka-sync}")
    private boolean isKafkaEnabled;

    @Override
    public PhraseOutput getRandomPhrase() {
        return phraseStorage.getRandomPhrase();
    }

    @Override
    public PhraseOutput saveInputPhrase(PhraseInput phraseInput) {
        if (!isKafkaEnabled) {
            phraseStorage.addPhrase(phraseInput.phrase());
            return new PhraseOutput(phraseInput.phrase());
        }
        return kafkaService.saveInputPhrase("addPhrases", phraseInput);
    }

    @Override
    public void clearPhrases() {
        phraseStorage.clearPhrases();
    }
}
