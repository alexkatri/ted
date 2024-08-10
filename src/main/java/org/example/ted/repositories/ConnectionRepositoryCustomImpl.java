package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ConnectionRepositoryCustomImpl implements ConnectionRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<User> findByUser(User user) {
        Query query = entityManager.createQuery("SELECT c.connection_user FROM Connection c WHERE c.user = ?1");
        query.setParameter(1, user);
        List<User> connected_users = query.getResultList();
        return connected_users;
    }
}
