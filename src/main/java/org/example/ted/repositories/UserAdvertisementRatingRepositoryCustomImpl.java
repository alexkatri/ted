package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Advertisement;
import org.example.ted.models.Comment;
import org.example.ted.models.Like;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserAdvertisementRatingRepositoryCustomImpl implements UserAdvertisementRatingRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Advertisement> getUserRecommendations(User user) {
        Query query = entityManager.createQuery("SELECT a FROM UserAdvertisementRating r, Advertisement a " +
                "WHERE r.advertisement = a AND " +
                "r.user = ?1 AND r.value >= 1 and r.state = ?2");
        query.setParameter(1, user);
        query.setParameter(2, "unknown");
        List<Advertisement> recommendations = query.getResultList();
        return recommendations;
    }
}
