package org.example.ted.repositories;


import org.example.ted.models.Article;
import org.example.ted.models.User;

import java.util.List;

public interface UserArticleRatingRepositoryCustom {
    // returns factorized matrix R
    double[][] getMatrix();
    List<Article> getRecommendedArticlesByUser(User user);
}
