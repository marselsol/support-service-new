//package com.example.servlets;
//
//import com.example.repository.PhraseStorage;
//import com.example.utils.beans.factory.BeanFactorySupportService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.io.BufferedReader;
//import java.io.PrintWriter;
//import java.io.StringReader;
//import java.io.StringWriter;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class HelpServletTest {
//
//    @Mock
//    private HttpServletRequest request;
//    @Mock
//    private HttpServletResponse response;
//
//    private HelpServlet servlet;
//    private StringWriter responseWriter;
//    private PhraseStorage phraseStorage;
//
//
//    @BeforeEach
//    void setUp() throws Exception {
//        BeanFactorySupportService beanFactory = new BeanFactorySupportService();
//        beanFactory.fillSingletonsMap("com.example");
//        beanFactory.fillAutowired();
//
//        MockitoAnnotations.initMocks(this);
//        servlet = new HelpServlet();
//        phraseStorage = new PhraseStorage();
//        servlet.setPhraseStorage(new PhraseStorage());
//        responseWriter = new StringWriter();
//        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
//        phraseStorage.clearPhrases();
//    }
//
//    @Test
//    void doGetWithNoPhrases() throws Exception {
//        servlet.doGet(request, response);
//        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "No phrases available.");
//    }
//
//    @Test
//    void doGetWithPhrases() throws Exception {
//        phraseStorage.addPhrase("Test phrase");
//        servlet.doGet(request, response);
//        assertTrue(responseWriter.toString().contains("Test phrase"));
//    }
//
//    @Test
//    void doPostWithEmptyPhrase() throws Exception {
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));
//        servlet.doPost(request, response);
//        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Phrase is empty!");
//    }
//
//    @Test
//    void doPostWithValidPhrase() throws Exception {
//        String testPhrase = "Test phrase";
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(testPhrase)));
//
//        servlet.doPost(request, response);
//        verify(response).setStatus(HttpServletResponse.SC_CREATED);
//
//        servlet.doGet(request, response);
//        assertTrue(responseWriter.toString().contains(testPhrase));
//    }
//}
