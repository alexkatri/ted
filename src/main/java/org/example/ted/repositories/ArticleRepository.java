package org.example.ted.repositories;

import org.example.ted.models.Article;
import org.example.ted.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    List<Article> findByCreatedBy(User user);

    @Query(value = "SELECT MAX(a.id) FROM Article a")
    int findMaxId();
}
