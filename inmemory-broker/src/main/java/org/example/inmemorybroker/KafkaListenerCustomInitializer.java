package org.example.inmemorybroker;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@EnableScheduling
public class KafkaListenerCustomInitializer {

    private final DefaultMessageBroker defaultMessageBroker;

    private final Map<String, Map<Object, Method>> registeredListeners = new ConcurrentHashMap<>();

    public KafkaListenerCustomInitializer(DefaultMessageBroker defaultMessageBroker) {
        this.defaultMessageBroker = defaultMessageBroker;
    }

    @Scheduled(fixedRate = 500)
    public void pollMessages() {
        registeredListeners.forEach((topicName, listeners) -> {
            List<String> messages = defaultMessageBroker.consumeMessage(topicName);
            if (!messages.isEmpty()) {
                listeners.forEach((bean, method) -> invokeListenerMethod(bean, method, messages));
            }
        });
    }

    private void invokeListenerMethod(Object bean, Method method, List<String> messages) {
        for (String message : messages) {
            try {
                method.setAccessible(true);
                method.invoke(bean, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerListener(String topicName, Object bean, Method method) {
        registeredListeners.computeIfAbsent(topicName, k -> new ConcurrentHashMap<>()).put(bean, method);
    }
}