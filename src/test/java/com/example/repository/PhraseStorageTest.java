package com.example.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PhraseStorageTest {
    @Test
    void testAddAndGetPhrase() {
        PhraseStorage.addPhrase("Test phrase!");
        String phrase = PhraseStorage.getRandomPhrase();
        assertNotNull(phrase);
        assertEquals("Test phrase!", phrase);
    }
}
