package com.example.controllers;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import com.example.services.PhraseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/help-service/v1/support")
@RequiredArgsConstructor
public class HelpController {

    private final PhraseServiceImpl phraseService;

    @GetMapping
    public PhraseOutput returnRandomPhrase() {
        PhraseOutput phraseOutput = phraseService.getRandomPhrase();
        log.info("Random phrase retrieved successfully.");
        return phraseOutput;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhraseOutput savePhrase(@RequestBody PhraseInput phraseInput) {
        PhraseOutput savedPhrase = phraseService.saveInputPhrase(phraseInput);
        log.info("Phrase saved successfully: {}", savedPhrase.phrase());
        return savedPhrase;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearPhrases() {
        phraseService.clearPhrases();
        log.info("All phrases cleared successfully.");
    }
}