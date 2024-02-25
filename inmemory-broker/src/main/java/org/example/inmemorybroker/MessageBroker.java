package org.example.inmemorybroker;

import java.util.List;


public interface MessageBroker {
    void createTopic(String topicName);

    void sendMessage(String topicName, String message);

    List<String> consumeMessage(String topicName);
}
