package org.example.ted.repositories;

import org.example.ted.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserArticleRatingRepository extends JpaRepository<UserArticleRating, UserArticleRatingId>, UserArticleRatingRepositoryCustom {
    // returns user ratings
    List<UserArticleRating> findByUser(User user);

    UserArticleRating findByUserAndArticle(User user, Article article);
    // returns 1 if "user" has rated "article"
    boolean existsByUserAndArticle(User user, Article article);
}
