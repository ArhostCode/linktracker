package edu.java.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(long chatId) {
        super("Чат %d не найден".formatted(chatId));
    }
}
