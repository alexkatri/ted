package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Comment;
import org.example.ted.models.Like;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Like> getNotificationLikes(User user) {
        Query query = entityManager.createQuery("SELECT l FROM Like l, Article a " +
                "WHERE a.createdBy = ?1 AND a = l.article");
        query.setParameter(1, user);
        List<Like> likes = query.getResultList();
        return likes;
    }

    @Override
    public List<Comment> getNotificationComments(User user) {
        Query query = entityManager.createQuery("SELECT c FROM Comment c, Article a " +
                "WHERE a.createdBy = ?1 AND a = c.article");
        query.setParameter(1, user);
        List<Comment> comments = query.getResultList();
        return comments;
    }
}
