package org.example.ted.exceptions;

public class ArticleMultimediaNotFoundException extends RuntimeException {

    public ArticleMultimediaNotFoundException(Long id) {
        super("Could not find multimedia " + id);
    }
}
