package org.example.ted.repositories;

import org.example.ted.models.Advertisement;
import org.example.ted.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, AdvertisementRepositoryCustom {
    List<Advertisement> findByCreatedBy(User created_by);

    @Query(value = "SELECT MAX(a.id) FROM Advertisement a")
    int findMaxId();
}
