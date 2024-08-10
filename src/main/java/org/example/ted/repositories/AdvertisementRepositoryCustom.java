package org.example.ted.repositories;


import org.example.ted.models.Advertisement;
import org.example.ted.models.User;

import java.util.List;

public interface AdvertisementRepositoryCustom {
    List<Advertisement> findByUser(User created_by);
}
