package org.example.ted.exceptions;

public class LikeNotFoundException extends RuntimeException {

    public LikeNotFoundException(Long id) {
        super("Could not find like " + id);
    }
}
