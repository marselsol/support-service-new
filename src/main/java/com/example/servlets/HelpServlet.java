package com.example.servlets;

import com.example.repository.PhraseStorage;
import com.example.utils.beans.factory.BeanFactoryMarsel;
import com.example.utils.beans.factory.annotation.AutowiredMarsel;
import com.example.utils.beans.factory.stereotype.ComponentMarsel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.NoSuchElementException;

@ComponentMarsel
public class HelpServlet extends HttpServlet {

    @AutowiredMarsel
    PhraseStorage phraseStorage;

    public void setPhraseStorage(PhraseStorage phraseStorage) {
        this.phraseStorage = phraseStorage;
        System.out.println("туууууууууууут: " + this.phraseStorage);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        try {
            resp.getWriter().println(phraseStorage.getRandomPhrase());
        } catch (NoSuchElementException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No phrases available.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String phrase = req.getReader().readLine();
        if (phrase == null || phrase.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Phrase is empty!");
            return;
        }
        BeanFactoryMarsel beanFactoryMarsel = new BeanFactoryMarsel();
        beanFactoryMarsel.getInfoAboutSingletonsMap("doPost");
        System.out.println("Проверка существования phraseStorage: " + this.phraseStorage);
        phraseStorage.addPhrase(phrase);
        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().println("The phrase has been added to the collection!");
    }
}
