package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Advertisement;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AdvertisementRepositoryCustomImpl implements AdvertisementRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Advertisement> findByUser(User created_by) {
        Query query = entityManager.createQuery("SELECT a FROM Advertisement a WHERE a.createdBy = ?1");
        query.setParameter(1, created_by);
        List<Advertisement> advertisements = query.getResultList();
        return advertisements;
    }

    @Override
    public List<Advertisement> showAdvertisements(User user) {
        Query query = entityManager.createQuery("SELECT a FROM Advertisement a WHERE a " +
                        "NOT IN (SELECT b FROM Advertisement b WHERE b.createdBy = ?1)");
        query.setParameter(1, user);
        List<Advertisement> advertisements = query.getResultList();
        return advertisements;
    }
}
