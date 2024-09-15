package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Article;
import org.example.ted.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final UserArticleRatingRepository userArticleRatingRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public ArticleRepositoryCustomImpl(@Lazy UserArticleRatingRepository userArticleRatingRepository) {
        this.userArticleRatingRepository = userArticleRatingRepository;
    }

    @Override
    public List<Article> userFeed(int id) {
        User user = entityManager.find(User.class, id);
        Query query = entityManager.createQuery("SELECT a FROM Article a WHERE a in (" +
                "SELECT a " +
                "FROM Article a " +
                "WHERE a.createdBy = ?1 " +
                "UNION " +
                "SELECT a FROM Article a, Connection con " +
                "WHERE ((con.user = ?1 AND a.createdBy = con.connection_user) OR " +
                "(con.connection_user = ?1 AND a.createdBy = con.user)) " +
                "UNION " +
                "SELECT a FROM Article a, Connection con, Like l " +
                "WHERE a = l.article AND " +
                "((con.user = ?1 AND l.createdBy = con.connection_user) OR " +
                "(con.connection_user = ?1 AND l.createdBy = con.user)))" +
                "order by a.date desc");
        query.setParameter(1, user);
        List<Article> articles = query.getResultList();
        // add recommended articles
        List<Article> recommendedArticles = userArticleRatingRepository.getRecommendedArticlesByUser(user);
        for (Article article : recommendedArticles) {
            if (articles.contains(article)) {
                continue;
            }
            articles.add(article);
        }
        return articles;
    }
}
