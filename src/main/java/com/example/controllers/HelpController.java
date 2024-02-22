package com.example.controllers;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import com.example.repository.PhraseStorage;
import com.example.utils.MessageBrokerKafService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/help-service/v1/support")
public class HelpController {

    private final PhraseStorage phraseStorage;
    private final MessageBrokerKafService messageBrokerKafService;

    @GetMapping
    public ResponseEntity<PhraseOutput> returnRandomPhrase() {
        return ResponseEntity.ok(phraseStorage.getRandomPhrase());
    }

    @PostMapping
    public ResponseEntity<PhraseOutput> savePhrase(@RequestBody PhraseInput phraseInput) {
        PhraseOutput savedPhrase = messageBrokerKafService.savePhrase("addPhrases", phraseInput.phrase());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPhrase);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearPhrases() {
        phraseStorage.clearPhrases();
        return ResponseEntity.noContent().build();
    }
}
