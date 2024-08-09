package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Article;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Article> findByUser(User user_email) {
//        List<Article> articles = null;
        Query query = entityManager.createQuery("SELECT a FROM Article a WHERE a.createdBy = ?1");
        query.setParameter(1, user_email);
        List<Article> articles = query.getResultList();
        return articles;
    }
}
