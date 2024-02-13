package com.example.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class PhraseStorageTest {
    private PhraseStorage phraseStorage;

    @BeforeEach
    void setUp() {
        phraseStorage = new PhraseStorage();
        phraseStorage.clearPhrases();
    }

    @Test
    void testAddAndGetPhrase() {
        phraseStorage.addPhrase("Test phrase!");
        String phrase = phraseStorage.getRandomPhrase();
        assertNotNull(phrase);
        assertEquals("Test phrase!", phrase);
    }

    @Test
    void testClearPhrases() {
        phraseStorage.addPhrase("Test phrase");
        phraseStorage.clearPhrases();

        assertThrows(NoSuchElementException.class, () -> {
            phraseStorage.getRandomPhrase();
        }, "Should throw NoSuchElementException if storage was cleared and is now empty");
    }
}
