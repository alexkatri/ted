package org.example.ted.exceptions;

public class AdvertisementNotFoundException extends RuntimeException {

    public AdvertisementNotFoundException(Long id) {
        super("Could not find advertisement " + id);
    }
}
