package org.example.ted.repositories;

import org.example.ted.models.Image;
import org.example.ted.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>/*, ImageRepositoryCustom*/ {
    Image findByUser(User user);
}