package com.example.kafkacustom;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConsumerKafkaCustom {
    private String topicName;
    private int offset;
}
