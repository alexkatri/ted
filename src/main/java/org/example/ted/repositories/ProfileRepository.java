package org.example.ted.repositories;

import org.example.ted.models.Profile;
import org.example.ted.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long>/*, ProfileRepositoryCustom*/ {
    Profile findByCreatedBy(User user);
}
