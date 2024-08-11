package org.example.ted.repositories;


import org.example.ted.models.Request;
import org.example.ted.models.User;

import java.util.List;

public interface RequestRepositoryCustom {
    List<Request> findByReceiver(User receiver);
}
