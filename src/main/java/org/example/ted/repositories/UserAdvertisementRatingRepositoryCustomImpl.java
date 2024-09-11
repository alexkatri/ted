package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Advertisement;
import org.example.ted.models.User;
import org.example.ted.recommendation.MatrixFactorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserAdvertisementRatingRepositoryCustomImpl implements UserAdvertisementRatingRepositoryCustom {

    private final UserAdvertisementRatingRepository userAdvertisementRatingRepository;
    private final AdvertisementRepository advertisementRepository;
    private MatrixFactorization matrixFactorization;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public UserAdvertisementRatingRepositoryCustomImpl(@Lazy UserAdvertisementRatingRepository userAdvertisementRatingRepository, AdvertisementRepository advertisementRepository, MatrixFactorization matrixFactorization) {
        this.userAdvertisementRatingRepository = userAdvertisementRatingRepository;
        this.advertisementRepository = advertisementRepository;
        this.matrixFactorization = matrixFactorization;
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
    public double[][] getMatrix(int K) {
        return matrixFactorization.factorization(K);
    }

    @Override
    public List<Advertisement> getRecommendedAdvertisementsByUser(User user) {
        //
        double[][] R = matrixFactorization.factorization(5);
//        System.out.println("factorization OK");
        List<Advertisement> recommendedAdvertisements = new ArrayList<>();
        double[] userVector = matrixFactorization.getUserVector(R, user.getId());
//        System.out.println(Arrays.toString(userVector));
        for (int i = 1; i <= userVector.length-1; i++) {
            if (userVector[i] == 1) {
                Advertisement ad = advertisementRepository.findById(Long.valueOf(i)).orElse(null);
                if (userAdvertisementRatingRepository.existsByUserAndAdvertisement(user, ad)) {
                    continue;
                }
                recommendedAdvertisements.add(ad);
            }
        }
        return recommendedAdvertisements;
    }
}
