package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.ted.models.Article;
import org.example.ted.models.User;
import org.example.ted.recommendation.ArticleRecommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class UserArticleRatingRepositoryCustomImpl implements UserArticleRatingRepositoryCustom {
    private final UserArticleRatingRepository userArticleRatingRepository;
    private final ArticleRepository articleRepository;
    private final ArticleRecommendation articleRecommendation;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public UserArticleRatingRepositoryCustomImpl(@Lazy UserArticleRatingRepository userArticleRatingRepository, ArticleRepository articleRepository, ArticleRecommendation articleRecommendation) {
        this.userArticleRatingRepository = userArticleRatingRepository;
        this.articleRepository = articleRepository;
        this.articleRecommendation = articleRecommendation;
    }

    @Override
    public double[][] getMatrix() {
        return articleRecommendation.getMatrixR();
    }

    @Override
    public List<Article> getRecommendedArticlesByUser(User user) {
        double[][] R = articleRecommendation.getMatrixR();
        List<Article> recommendedArticles = new ArrayList<>();
        double[] userVector = articleRecommendation.getUserVector(R, user.getId());
        for (int i = 1; i <= userVector.length-1; i++) {
            if (userVector[i] == 1) {
                Article ar = articleRepository.findById((long) i).orElse(null);
                if (userArticleRatingRepository.existsByUserAndArticle(user, ar) || Objects.requireNonNull(ar).getCreatedBy().equals(user)) {
                    continue;
                }
                recommendedArticles.add(ar);
            }
        }
        return recommendedArticles;
    }

}
