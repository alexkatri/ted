package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Advertisement;
import org.example.ted.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class AdvertisementRepositoryCustomImpl implements AdvertisementRepositoryCustom {

    private final UserAdvertisementRatingRepository userAdvertisementRatingRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public AdvertisementRepositoryCustomImpl(@Lazy UserAdvertisementRatingRepository userAdvertisementRatingRepository) {
        this.userAdvertisementRatingRepository = userAdvertisementRatingRepository;
    }

    @Override
    public List<Advertisement> showAdvertisements(Optional<User> user) {
        User userEntity = user.get();
        Query query = entityManager.createQuery("SELECT a FROM Advertisement a WHERE a " +
                        "NOT IN (SELECT b FROM Advertisement b WHERE b.createdBy = ?1)");
        query.setParameter(1, userEntity);
        List<Advertisement> advertisements = query.getResultList();
        List<Advertisement> recommendedAdvertisements = userAdvertisementRatingRepository.getRecommendedAdvertisementsByUser(userEntity);
        for (Advertisement advertisement : recommendedAdvertisements) {
            if (advertisements.contains(advertisement)) {
                continue;
            }
            advertisements.add(advertisement);
        }
        return advertisements;
    }
}
