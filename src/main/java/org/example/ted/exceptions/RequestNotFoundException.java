package org.example.ted.exceptions;

public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(Long id) {
        super("Could not find request " + id);
    }
}
