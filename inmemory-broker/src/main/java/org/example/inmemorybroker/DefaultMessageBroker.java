package org.example.inmemorybroker;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DefaultMessageBroker implements MessageBroker {
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> topics = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConsumerKafkaCustom> consumersKafkaCustom = new ConcurrentHashMap<>();

    @Override
    public void createTopic(String topicName) {
        topics.put(topicName, new CopyOnWriteArrayList<>());
    }

    @Override
    public void sendMessage(String topicName, String message) {
        if (!topics.containsKey(topicName)) {
            createTopic(topicName);
        }
        topics.get(topicName).add(message);
    }

    @Override
    public List<String> consumeMessage(String topicName) {
        if (!consumersKafkaCustom.containsKey(topicName)) {
            consumersKafkaCustom.put(topicName, new ConsumerKafkaCustom());
        }
        ConsumerKafkaCustom consumer = consumersKafkaCustom.get(topicName);
        if (!topics.containsKey(topicName)) {
            return new ArrayList<>();
        }
        ArrayList<String> messagesToConsume = new ArrayList<>();
        for (int i = consumer.getOffset(); i < topics.get(topicName).size(); i++) {
            messagesToConsume.add(topics.get(topicName).get(i));
            consumer.setOffset(i + 1);
        }
        return messagesToConsume;
    }
}
