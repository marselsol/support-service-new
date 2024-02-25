package com.example.repository;

import com.example.dto.PhraseOutput;
import lombok.extern.slf4j.Slf4j;
import org.example.inmemorybroker.KafkaListenerCustom;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Repository
public class PhraseStorage {
    private static final List<String> phrases = new CopyOnWriteArrayList<>();

    @KafkaListenerCustom(topicName = "addPhrases")
    public void addPhrasesMethodListener(String message) {
        addPhrase(message);
        log.info("Read the message and added it to phraseStorage: {}", message);
    }

    public PhraseOutput addPhrase(String phrase) {
        if (phrase == null || phrase.trim().isEmpty()) {
            log.error("Attempted to add an empty phrase.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input phrase is empty!");
        }
        phrases.add(phrase);
        log.info("New phrase added: {}", phrase);
        return new PhraseOutput(phrase);
    }

    public PhraseOutput getRandomPhrase() {
        if (phrases.isEmpty()) {
            log.error("No phrases available to return.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No phrases found");
        }
        String phrase = phrases.get(new Random().nextInt(phrases.size()));
        log.info("Output of the requested phrase: {}", phrase);
        return new PhraseOutput(phrase);
    }

    public void clearPhrases() {
        phrases.clear();
    }
}