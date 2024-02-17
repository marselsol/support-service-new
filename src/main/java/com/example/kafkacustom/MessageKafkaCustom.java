package com.example.kafkacustom;

import java.time.Instant;

public record MessageKafkaCustom(String message, Instant messageTime) {
}
