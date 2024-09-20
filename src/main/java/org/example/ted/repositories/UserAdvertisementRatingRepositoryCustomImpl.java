package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Advertisement;
import org.example.ted.models.User;
import org.example.ted.recommendation.AdvertisementRecommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional(readOnly = true)
public class UserAdvertisementRatingRepositoryCustomImpl implements UserAdvertisementRatingRepositoryCustom {

    private final UserAdvertisementRatingRepository userAdvertisementRatingRepository;
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementRecommendation advertisementRecommendation;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public UserAdvertisementRatingRepositoryCustomImpl(@Lazy UserAdvertisementRatingRepository userAdvertisementRatingRepository, AdvertisementRepository advertisementRepository, AdvertisementRecommendation advertisementRecommendation) {
        this.userAdvertisementRatingRepository = userAdvertisementRatingRepository;
        this.advertisementRepository = advertisementRepository;
        this.advertisementRecommendation = advertisementRecommendation;
    }

    @Override
    public List<Advertisement> getKnownAdvertisementsByUser(User user) {
        Query query = entityManager.createQuery("SELECT a FROM UserAdvertisementRating r, Advertisement a " +
                "WHERE r.advertisement = a AND " +
                "r.user = ?1 AND r.value >= 1");
        query.setParameter(1, user);
        List<Advertisement> advertisements = query.getResultList();
        return advertisements;
    }

    @Override
    public double[][] getMatrix() {
        return advertisementRecommendation.getMatrixR();
    }

    @Override
    public List<Advertisement> getRecommendedAdvertisementsByUser(User user) {
        List<Advertisement> recommendedAdvertisements = new ArrayList<>();
        double[][] R = advertisementRecommendation.getMatrixR();
        double[] userVector = advertisementRecommendation.getUserVector(R, user.getId());
        if (userVector == null) {
            return recommendedAdvertisements;
        }
        for (int i = 1; i <= userVector.length-1; i++) {
            if (userVector[i] == 1) {
                Advertisement ad = advertisementRepository.findById((long) i).orElse(null);
                if (userAdvertisementRatingRepository.existsByUserAndAdvertisement(user, ad) || Objects.requireNonNull(ad).getCreatedBy().equals(user)) {
                    continue;
                }
                recommendedAdvertisements.add(ad);
            }
        }
        return recommendedAdvertisements;
    }
}
