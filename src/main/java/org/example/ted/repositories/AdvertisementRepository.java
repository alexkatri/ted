package org.example.ted.repositories;

import org.example.ted.models.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, AdvertisementRepositoryCustom {
}
