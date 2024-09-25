package org.example.ted.repositories;

import org.example.ted.models.Advertisement;
import org.example.ted.models.User;
import org.example.ted.models.UserAdvertisementRating;
import org.example.ted.models.UserAdvertisementRatingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdvertisementRatingRepository extends JpaRepository<UserAdvertisementRating, UserAdvertisementRatingId>, UserAdvertisementRatingRepositoryCustom {
    // returns 1 if "user" has visited "advertisement"
    boolean existsByUserAndAdvertisement(User user, Advertisement advertisement);

    UserAdvertisementRating findByUserAndAdvertisement(User user, Advertisement advertisement);
}
