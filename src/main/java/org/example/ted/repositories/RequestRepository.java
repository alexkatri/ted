package org.example.ted.repositories;

import org.example.ted.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long>, RequestRepositoryCustom {
}
