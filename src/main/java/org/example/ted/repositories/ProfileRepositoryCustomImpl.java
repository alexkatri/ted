package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Profile;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Profile findByUser(User created_by) {
        Profile profile = null;
        Query query = entityManager.createQuery("SELECT p FROM Profile p WHERE p.createdBy = ?1");
        query.setParameter(1, created_by);
        List<Profile> profiles = query.getResultList();
        if (profiles != null && profiles.size() > 0)
            profile = profiles.get(0);
        return profile;
    }
}
