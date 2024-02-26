package com.example.services;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;

/**
 * Интерфейс KafkaService определяет контракт для взаимодействия с Kafka для сохранения и обработки фраз.
 */
public interface KafkaService {
    /**
     * Сохраняет входящую фразу в Kafka по указанному имени топика.
     *
     * @param topicName имя Kafka топика, в который следует сохранить сообщение.
     * @param message   объект {@link PhraseInput}, содержащий фразу для сохранения.
     * @return объект {@link PhraseOutput}, содержащий сохраненную фразу.
     * @throws IllegalArgumentException если topicName или message являются null или пустыми.
     */
    PhraseOutput saveInputPhrase(String topicName, PhraseInput message);

    /**
     * Обработчик сообщений для Kafka, вызывается при появлении новых сообщений в топике.
     *
     * @param message объект {@link PhraseInput}, содержащий фразу для обработки.
     */
    void phrasesMethodListener(PhraseInput message);
}