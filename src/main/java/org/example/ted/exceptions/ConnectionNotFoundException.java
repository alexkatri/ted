package org.example.ted.exceptions;

public class ConnectionNotFoundException extends RuntimeException {

    public ConnectionNotFoundException(Long id) {
        super("Could not find connection " + id);
    }
}
