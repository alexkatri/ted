package org.example.ted.repositories;

import org.example.ted.models.Article;
import org.example.ted.models.Like;
import org.example.ted.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long>/*, LikeRepositoryCustom*/ {
    List<Like> findByCreatedBy(User user);
    List<Like> findByArticle(Article article);
}
