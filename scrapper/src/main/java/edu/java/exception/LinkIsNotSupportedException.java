package edu.java.exception;

import java.net.URI;
import org.springframework.http.HttpStatus;

public class LinkIsNotSupportedException extends ScrapperException {

    public LinkIsNotSupportedException(URI link) {
        super(
            "Ссылка не поддерживается",
            "Ссылка %s не поддерживается".formatted(link.toString()),
            HttpStatus.BAD_REQUEST
        );
    }
}
