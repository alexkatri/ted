package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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
    public List<Notification> findByUser(User user) {
        Query query = entityManager.createQuery("SELECT n FROM Notification n WHERE n.user = ?1");
        query.setParameter(1, user);
        List<Notification> notifications = query.getResultList();
        return notifications;
    }
}
