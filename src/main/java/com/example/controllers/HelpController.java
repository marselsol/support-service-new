package com.example.controllers;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import com.example.services.PhraseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/help-service/v1/support")
@RequiredArgsConstructor
public class HelpController {

    private final PhraseServiceImpl phraseServiceImpl;

    @GetMapping
    public ResponseEntity<PhraseOutput> returnRandomPhrase() {
        try {
            PhraseOutput phraseOutput = phraseServiceImpl.getRandomPhrase();
            log.info("Random phrase retrieved successfully.");
            return ResponseEntity.ok(phraseOutput);
        } catch (ResponseStatusException e) {
            log.error("Error retrieving random phrase: {}", e.getReason());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<PhraseOutput> savePhrase(@RequestBody PhraseInput phraseInput) {
        try {
            PhraseOutput savedPhrase = phraseServiceImpl.saveInputPhrase(phraseInput);
            log.info("Phrase saved successfully: {}", savedPhrase.phrase());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPhrase);
        } catch (UnsupportedOperationException e) {
            log.error("Kafka service is disabled: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new PhraseOutput(e.getMessage()));
        } catch (ResponseStatusException e) {
            log.error("Error saving phrase: {}", e.getReason());
            throw e;
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> clearPhrases() {
        try {
            phraseServiceImpl.clearPhrases();
            log.info("All phrases cleared successfully.");
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            log.error("Error clearing phrases: {}", e.getReason());
            throw e;
        }
    }
}