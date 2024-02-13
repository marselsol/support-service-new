package com.example.controllers;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import com.example.repository.PhraseStorage;
import com.example.utils.beans.factory.annotation.AutowiredSupportService;
import com.example.utils.beans.factory.stereotype.ControllerSupportServiceAnnotation;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.NoSuchElementException;

@ControllerSupportServiceAnnotation
public class HelpController implements ControllerSupportService {

    @AutowiredSupportService
    private PhraseStorage phraseStorage;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setPhraseStorage(PhraseStorage phraseStorage) {
        this.phraseStorage = phraseStorage;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            doPost(request, response);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            PhraseOutput phraseResponse = new PhraseOutput(phraseStorage.getRandomPhrase());
            resp.getWriter().write(objectMapper.writeValueAsString(phraseResponse));
        } catch (NoSuchElementException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "{\"error\":\"No phrases available.\"}");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PhraseInput input = objectMapper.readValue(req.getInputStream(), PhraseInput.class);
        String phrase = input.getPhrase();
        if (phrase == null || phrase.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Phrase is empty!\"}");
            return;
        }
        phraseStorage.addPhrase(phrase);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().println("{\"message\":\"The phrase has been added to the collection!\"}");
    }
}