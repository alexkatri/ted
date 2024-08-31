package org.example.ted.repositories;

import org.example.ted.models.User;
import org.example.ted.models.UserAdvertisementRating;
import org.example.ted.models.UserAdvertisementRatingId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAdvertisementRatingRepository extends JpaRepository<UserAdvertisementRating, UserAdvertisementRatingId> {
    List<UserAdvertisementRating> findByUser(User user);    // returns user ratings
}
