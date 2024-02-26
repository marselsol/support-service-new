package com.example.controllers;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import com.example.repository.PhraseStorageImpl;
import com.example.services.KafkaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/help-service/v1/support")
public class HelpController {

    @Autowired
    private PhraseStorageImpl phraseStorage;
    @Autowired(required = false)
    private KafkaServiceImpl kafkaService;


    @GetMapping
    public ResponseEntity<PhraseOutput> returnRandomPhrase() {
        return ResponseEntity.ok(phraseStorage.getRandomPhrase());
    }

    @PostMapping
    public ResponseEntity<PhraseOutput> savePhrase(@RequestBody PhraseInput phraseInput) {
        PhraseOutput savedPhrase = kafkaService.saveInputPhrase("addPhrases", phraseInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPhrase);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearPhrases() {
        phraseStorage.clearPhrases();
        return ResponseEntity.noContent().build();
    }
}
