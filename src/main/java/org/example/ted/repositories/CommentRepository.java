package org.example.ted.repositories;

import org.example.ted.models.Article;
import org.example.ted.models.Comment;
import org.example.ted.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>/*, CommentRepositoryCustom*/ {
    List<Comment> findByCreatedBy(User user);
    List<Comment> findByArticle(Article article);
}
