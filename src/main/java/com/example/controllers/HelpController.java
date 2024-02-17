package com.example.controllers;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import com.example.repository.PhraseStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/help-service/v1/support")
public class HelpController {

    private final PhraseStorage phraseStorage;

    @GetMapping
    public ResponseEntity<PhraseOutput> returnRandomPhrase() {
        return ResponseEntity.ok(phraseStorage.getRandomPhrase());
    }

    @PostMapping
    public ResponseEntity<PhraseOutput> savePhrase(@RequestBody PhraseInput phraseInput) {
        return ResponseEntity.ok(phraseStorage.addPhrase(phraseInput.phrase()));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearPhrases() {
        phraseStorage.clearPhrases();
        return ResponseEntity.ok().build();
    }
}
