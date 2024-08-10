package org.example.ted.repositories;


import org.example.ted.models.Advertisement;
import org.example.ted.models.Notification;
import org.example.ted.models.User;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<Notification> findByUser(User user);
}
