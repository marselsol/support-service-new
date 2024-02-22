package org.example.inmemorybroker;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
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

    private final ApplicationContext context;

    private final Map<String, Map<Object, Method>> registeredListeners = new ConcurrentHashMap<>();

    public KafkaListenerCustomInitializer(DefaultMessageBroker defaultMessageBroker, ApplicationContext context) {
        this.defaultMessageBroker = defaultMessageBroker;
        this.context = context;
    }

    @PostConstruct
    public void init() {
        discoverListeners();
    }



    private void discoverListeners() {
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(KafkaListenerCustom.class)) {
                    KafkaListenerCustom annotation = method.getAnnotation(KafkaListenerCustom.class);
                    registeredListeners.computeIfAbsent(annotation.topicName(), k -> new ConcurrentHashMap<>()).put(bean, method);
                }
            }
        }
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
}