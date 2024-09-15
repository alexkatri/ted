package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Connection;
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
    public List<Connection> findByUser(User user) {
        Query query = entityManager.createQuery("SELECT c FROM Connection c WHERE c.user = ?1 or c.connection_user = ?1");
        query.setParameter(1, user);
        List<Connection> connections = query.getResultList();
        return connections;
    }
}
