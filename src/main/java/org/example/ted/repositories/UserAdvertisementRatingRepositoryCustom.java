package org.example.ted.repositories;


import org.example.ted.models.Advertisement;
import org.example.ted.models.User;

import java.util.List;

public interface UserAdvertisementRatingRepositoryCustom {
    // returns list of the advertisements visited by "user"
    List<Advertisement> getKnownAdvertisementsByUser(User user);
    // returns factorized matrix r
    double[][] getMatrix(int K);
    List<Advertisement> getRecommendedAdvertisementsByUser(User user);
}
