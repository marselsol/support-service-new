package com.example.services;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;

public interface PhraseService {
    PhraseOutput getRandomPhrase();

    PhraseOutput saveInputPhrase(PhraseInput phraseInput);

    void clearPhrases();
}
