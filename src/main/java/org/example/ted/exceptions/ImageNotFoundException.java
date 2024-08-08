package org.example.ted.exceptions;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(Long id) {
        super("Could not find image " + id);
    }
    public ImageNotFoundException(String user_email) {
        super("Could not find image of user " + user_email);
    }
}
