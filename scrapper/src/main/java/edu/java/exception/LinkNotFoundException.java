package edu.java.exception;

import org.springframework.http.HttpStatus;

public class LinkNotFoundException extends ScrapperException {
    public LinkNotFoundException(long id) {
        super("Link with id " + id + " is not found", "Link not found", HttpStatus.NOT_FOUND);
    }
}
