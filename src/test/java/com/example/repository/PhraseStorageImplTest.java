package com.example.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public class PhraseStorageImplTest {
    private PhraseStorageImpl phraseStorageImpl;

    @BeforeEach
    void setUp() {
        phraseStorageImpl = new PhraseStorageImpl();
        phraseStorageImpl.clearPhrases();
    }

    @Test
    void testAddAndGetPhrase() {
        phraseStorageImpl.addPhrase("Test phrase!");
        String phrase = phraseStorageImpl.getRandomPhrase().phrase();
        assertNotNull(phrase);
        assertEquals("Test phrase!", phrase);
    }

    @Test
    void testClearPhrases() {
        phraseStorageImpl.addPhrase("Test phrase");
        phraseStorageImpl.clearPhrases();

        assertThrows(ResponseStatusException.class, () -> phraseStorageImpl.getRandomPhrase(), "Should throw ResponseStatusException if storage was cleared and is now empty");
    }
}