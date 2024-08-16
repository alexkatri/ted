package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Comment;
import org.example.ted.models.Like;
import org.example.ted.models.Notification;
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
    public List<Notification> findByReceiver(User receiver) {
        Query query = entityManager.createQuery("SELECT n FROM Notification n WHERE n.receiver = ?1");
        query.setParameter(1, receiver);
        List<Notification> notifications = query.getResultList();
        return notifications;
    }

    @Override
    public List<Like> getNotificationLikes(User user_email) {
        Query query = entityManager.createQuery("SELECT l FROM Like l, Article a " +
                "WHERE a.createdBy = ?1 AND a = l.article");
        query.setParameter(1, user_email);
        List<Like> likes = query.getResultList();
        return likes;
    }

    @Override
    public List<Comment> getNotificationComments(User user_email) {
        Query query = entityManager.createQuery("SELECT c FROM Comment c, Article a " +
                "WHERE a.createdBy = ?1 AND a = c.article");
        query.setParameter(1, user_email);
        List<Comment> comments = query.getResultList();
        return comments;
    }
}
