package edu.java.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ScrapperException extends RuntimeException {
    private final String description;
    private final HttpStatus status;

    /**
     * ScrapperException with description and message
     * @param description general description of the error
     * @param message a specific error message with the necessary clarifications
     * @param status HTTP status code
     */
    public ScrapperException(String description, String message, HttpStatus status) {
        super(message);
        this.description = description;
        this.status = status;
    }
}
