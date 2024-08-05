package org.example.ted.exceptions;

public class ProfileNotFoundException extends RuntimeException {

    public ProfileNotFoundException(Long id) {
        super("Could not find profile " + id);
    }
}
