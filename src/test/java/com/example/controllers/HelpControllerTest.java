package com.example.controllers;

import com.example.dto.PhraseInput;
import com.example.services.KafkaServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class HelpControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private KafkaServiceImpl kafkaServiceImpl;

    @Test
    void returnRandomPhrase_Success() throws Exception {
        kafkaServiceImpl.saveInputPhrase("addPhrases", new PhraseInput("Test phrase"));
        Thread.sleep(500);
        mockMvc.perform(get("/help-service/v1/support"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test phrase")));
    }

    @Test
    void savePhrase_Success() throws Exception {
        mockMvc.perform(post("/help-service/v1/support")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phrase\":\"New phrase\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("New phrase")));
    }
}