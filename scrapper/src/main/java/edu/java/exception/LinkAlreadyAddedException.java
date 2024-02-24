package edu.java.exception;

import java.net.URI;
import org.springframework.http.HttpStatus;

public class LinkAlreadyAddedException extends ScrapperException {
    public LinkAlreadyAddedException(URI link) {
        super(
            "Повторная регистрация ссылки",
            "Ссылка %s уже добавлена".formatted(link.toString()),
            HttpStatus.BAD_REQUEST
        );
    }
}
