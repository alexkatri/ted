package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Like;
import org.example.ted.models.Notification;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Like> findByUser(User created_by) {
        Query query = entityManager.createQuery("SELECT l FROM Like l WHERE l.createdBy = ?1");
        query.setParameter(1, created_by);
        List<Like> likes = query.getResultList();
        return likes;
    }
}
