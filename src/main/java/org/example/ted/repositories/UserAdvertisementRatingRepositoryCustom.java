package org.example.ted.repositories;


import org.example.ted.models.Advertisement;
import org.example.ted.models.Comment;
import org.example.ted.models.Like;
import org.example.ted.models.User;

import java.util.List;

public interface UserAdvertisementRatingRepositoryCustom {
    List<Advertisement> getUserRecommendations(User user);
}
