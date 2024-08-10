package org.example.ted.repositories;

import org.example.ted.models.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long>, ConnectionRepositoryCustom {
}
