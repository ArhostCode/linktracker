package edu.java.exception;

import org.springframework.http.HttpStatus;

public class ChatNotFoundException extends ScrapperException {
    public ChatNotFoundException(long chatId) {
        super("Отсутствует чат", "Чат %d не найден".formatted(chatId), HttpStatus.NOT_FOUND);
    }
}
