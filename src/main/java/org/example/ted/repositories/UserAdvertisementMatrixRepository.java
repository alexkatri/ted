package org.example.ted.repositories;

import org.example.ted.models.UserAdvertisementMatrix;
import org.example.ted.models.UserAdvertisementMatrixId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdvertisementMatrixRepository extends JpaRepository<UserAdvertisementMatrix, UserAdvertisementMatrixId> {
}
