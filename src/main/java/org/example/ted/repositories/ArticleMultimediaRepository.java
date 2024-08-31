package org.example.ted.repositories;

import org.example.ted.models.Article;
import org.example.ted.models.ArticleMultimedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleMultimediaRepository extends JpaRepository<ArticleMultimedia, Long> {
    ArticleMultimedia findByArticle(Article article);
}
