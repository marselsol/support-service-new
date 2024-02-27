package com.example.repository;

import com.example.dto.PhraseOutput;

public interface PhraseStorage {
    void addPhrase(String phrase);

    PhraseOutput getRandomPhrase();

    void clearPhrases();
}