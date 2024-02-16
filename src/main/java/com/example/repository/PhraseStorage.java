package com.example.repository;

import com.example.utils.beans.factory.stereotype.RepositorySupportService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@RepositorySupportService
public class PhraseStorage {
    private static final List<String> phrases = new CopyOnWriteArrayList<>();

    public void addPhrase(String phrase) {
        phrases.add(phrase);
        System.out.printf("New phrase added: %s\n", phrase);
    }

    public String getRandomPhrase() throws NoSuchElementException {
        if (phrases.isEmpty()) {
            throw new NoSuchElementException();
        }
        String phrase = phrases.get(new Random().nextInt(phrases.size()));
        System.out.printf("Output of the requested phrase: %s\n", phrase);
        return phrase;
    }

    public void clearPhrases() {
        phrases.clear();
    }
}
