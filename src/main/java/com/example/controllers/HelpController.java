package com.example.controllers;

import com.example.repository.PhraseStorage;
import com.example.utils.beans.factory.annotation.AutowiredSupportService;
import com.example.utils.beans.factory.stereotype.ControllerSupportServiceAnnotation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.NoSuchElementException;

@ControllerSupportServiceAnnotation
public class HelpController implements ControllerSupportService {

    @AutowiredSupportService
    private PhraseStorage phraseStorage;

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
        resp.setContentType("text/plain");
        try {
            resp.getWriter().println(phraseStorage.getRandomPhrase());
        } catch (NoSuchElementException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No phrases available.");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String phrase = req.getReader().readLine();
        if (phrase == null || phrase.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Phrase is empty!");
            return;
        }
        phraseStorage.addPhrase(phrase);
        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().println("The phrase has been added to the collection!");
    }
}