package edu.java.exception;

import org.springframework.http.HttpStatus;

public class ChatAlreadyRegisteredException extends ScrapperException {
    public ChatAlreadyRegisteredException(long chatId) {
        super("Повторная регистрация чата", "Чат %d уже зарегистрирован".formatted(chatId), HttpStatus.BAD_REQUEST);
    }
}
