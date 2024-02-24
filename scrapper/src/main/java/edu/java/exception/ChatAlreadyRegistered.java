package edu.java.exception;

public class ChatAlreadyRegistered extends RuntimeException {
    public ChatAlreadyRegistered(long chatId) {
        super("Чат %d уже зарегистрирован".formatted(chatId));
    }
}
