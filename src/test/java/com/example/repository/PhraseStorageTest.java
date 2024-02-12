//package com.example.repository;
//
//import com.example.servlets.HelpServlet;
//import com.example.utils.beans.factory.BeanFactorySupportService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class PhraseStorageTest {
//    private PhraseStorage phraseStorage;
//
//    @BeforeEach
//    void setUp() {
//        BeanFactorySupportService beanFactory = new BeanFactorySupportService();
//        beanFactory.fillSingletonsMap("com.example");
//        beanFactory.fillAutowired();
//
//        HelpServlet servlet = new HelpServlet();
//        servlet.setPhraseStorage(new PhraseStorage());
//        phraseStorage = new PhraseStorage();
//    }
//
//    @Test
//    void testAddAndGetPhrase() {
//        phraseStorage.addPhrase("Test phrase!");
//        String phrase = phraseStorage.getRandomPhrase();
//        assertNotNull(phrase);
//        assertEquals("Test phrase!", phrase);
//    }
//}
