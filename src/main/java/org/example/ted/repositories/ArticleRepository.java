package org.example.ted.repositories;

import org.example.ted.models.Article;
import org.example.ted.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    List<Article> findByCreatedBy(User user);
}
