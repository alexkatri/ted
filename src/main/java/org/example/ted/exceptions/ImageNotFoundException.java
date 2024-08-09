package org.example.ted.exceptions;

import org.example.ted.models.User;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(Long id) {
        super("Could not find image " + id);
    }
//    public ImageNotFoundException(User user_email) {
//        super("Could not find image of user " + user_email);
//    }
}
