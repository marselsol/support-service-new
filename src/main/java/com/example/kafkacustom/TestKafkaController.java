package com.example.kafkacustom;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestKafkaController {
    private final MessageBroker messageBroker;

    public TestKafkaController(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @GetMapping
    public List<String> getMessageByTopic() {
        return messageBroker.consumeMessage("test");
    }

    @PostMapping
    public void addMessage(@RequestBody String phrase) {
        messageBroker.sendMessage("addPhrases", phrase);
    }
}

