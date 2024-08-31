package org.example.ted.repositories;


import org.example.ted.models.Advertisement;
import org.example.ted.models.User;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepositoryCustom {
    List<Advertisement> showAdvertisements(Optional<User> user);
}
