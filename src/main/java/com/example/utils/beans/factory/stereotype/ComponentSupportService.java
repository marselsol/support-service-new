package com.example.utils.beans.factory.stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
/**
 * По умолчанию аннотации не загружаются в память во время работы программы.
 * Изменяем поведение через новую политику удержания (RetentionPolicy.RUNTIME).
 */
public @interface ComponentSupportService {
}
