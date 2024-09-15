package org.example.ted.repositories;


import org.example.ted.models.Connection;
import org.example.ted.models.User;

import java.util.List;

public interface ConnectionRepositoryCustom {
    List<Connection> findByUser(User user);
}
