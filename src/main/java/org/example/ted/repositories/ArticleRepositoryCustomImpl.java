package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Article;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public List<Article> findByUser(User user_email) {
//        Query query = entityManager.createQuery("SELECT a FROM Article a WHERE a.createdBy = ?1");
//        query.setParameter(1, user_email);
//        List<Article> articles = query.getResultList();
//        return articles;
//    }

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
        return articles;
    }
}
