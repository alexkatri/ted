package org.example.ted.repositories;

import org.example.ted.models.Request;
import org.example.ted.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>/*, RequestRepositoryCustom*/ {
    List<Request> findByReceiver(User receiver);
}
