package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Article;
import org.example.ted.models.User;
import org.example.ted.recommendation.ArticleRecommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserArticleRatingRepositoryCustomImpl implements UserArticleRatingRepositoryCustom {
    private final UserArticleRatingRepository userArticleRatingRepository;
    private final ArticleRepository articleRepository;
    private final ArticleRecommendation articleRecommendation;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public UserArticleRatingRepositoryCustomImpl(@Lazy UserArticleRatingRepository userArticleRatingRepository, AdvertisementRepository advertisementRepository, ArticleRepository articleRepository, ArticleRecommendation articleRecommendation) {
        this.userArticleRatingRepository = userArticleRatingRepository;
        this.articleRepository = articleRepository;
        this.articleRecommendation = articleRecommendation;
    }

    @Override
    public List<Article> getKnownArticlesByUser(User user) {
        Query query = entityManager.createQuery("SELECT a FROM UserArticleRating r, Article a " +
                "WHERE r.article = a AND " +
                "r.user = ?1 AND r.value >= 1");
        query.setParameter(1, user);
        List<Article> articles = query.getResultList();
        return articles;
    }

    @Override
    public double[][] getMatrix() {
        return articleRecommendation.getMatrixR();
    }

    @Override
    public List<Article> getRecommendedArticlesByUser(User user) {
        //
        double[][] R = articleRecommendation.getMatrixR();
//        System.out.println("factorization OK");
        List<Article> recommendedArticles = new ArrayList<>();
        double[] userVector = articleRecommendation.getUserVector(R, user.getId());
//        System.out.println(Arrays.toString(userVector));
        for (int i = 1; i <= userVector.length-1; i++) {
            if (userVector[i] == 1) {
                Article ar = articleRepository.findById(Long.valueOf(i)).orElse(null);
                if (userArticleRatingRepository.existsByUserAndArticle(user, ar)) {
                    continue;
                }
                recommendedArticles.add(ar);
            }
        }
        return recommendedArticles;
    }

}
