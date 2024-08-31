package org.example.ted.repositories;

import org.example.ted.models.Connection;
import org.example.ted.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, Long>/*, ConnectionRepositoryCustom*/ {
    List<Connection> findByUser(User user);
}
