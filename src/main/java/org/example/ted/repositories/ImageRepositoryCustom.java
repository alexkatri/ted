package org.example.ted.repositories;


import org.example.ted.models.Image;

public interface ImageRepositoryCustom {
    Image findByUserEmail(String user_email);
}
