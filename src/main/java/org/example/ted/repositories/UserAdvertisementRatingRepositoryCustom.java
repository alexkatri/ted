package org.example.ted.repositories;


import org.example.ted.models.Advertisement;
import org.example.ted.models.User;

import java.util.List;

public interface UserAdvertisementRatingRepositoryCustom {
    List<Advertisement> getKnownAdvertisements(User user);
    double[][] getMatrix(int K);
}
