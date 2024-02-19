package com.example.controllers;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import com.example.repository.PhraseStorage;
import com.example.utils.KafkaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(HelpController.class)
class HelpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhraseStorage phraseStorage;

    @MockBean
    private KafkaService kafkaService;

    @Test
    void returnRandomPhrase_Success() throws Exception {
        PhraseOutput mockPhrase = new PhraseOutput("Test phrase");
        when(phraseStorage.getRandomPhrase()).thenReturn(mockPhrase);

        mockMvc.perform(get("/help-service/v1/support"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test phrase")));
    }

    @Test
    void savePhrase_Success() throws Exception {
        PhraseOutput output = new PhraseOutput("New phrase");
        when(kafkaService.savePhrase(anyString(), anyString())).thenReturn(output);

        mockMvc.perform(post("/help-service/v1/support")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phrase\":\"New phrase\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("New phrase")));
    }
}