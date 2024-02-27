package com.example.services;

import com.example.dto.PhraseInput;
import com.example.dto.PhraseOutput;
import org.springframework.web.server.ResponseStatusException;

/**
 * Интерфейс PhraseService предоставляет методы для управления фразами.
 * Это включает в себя получение случайной фразы, сохранение новой фразы,
 * а также очистку всех существующих фраз.
 */
public interface PhraseService {
    /**
     * Получает случайную фразу из хранилища.
     *
     * @return PhraseOutput объект, содержащий случайно выбранную фразу.
     * @throws ResponseStatusException если фразы для получения нет.
     */
    PhraseOutput getRandomPhrase();

    /**
     * Сохраняет входящую фразу и возвращает ее.
     * В зависимости от настройки фича-тогла может также сохранять фразу в Kafka.
     *
     * @param phraseInput PhraseInput объект, содержащий фразу для сохранения.
     * @return PhraseOutput объект, содержащий сохраненную фразу.
     * @throws ResponseStatusException если фраза невалидна или не может быть сохранена.
     */
    PhraseOutput saveInputPhrase(PhraseInput phraseInput);

    /**
     * Очищает все сохраненные фразы из хранилища.
     */
    void clearPhrases();
}
