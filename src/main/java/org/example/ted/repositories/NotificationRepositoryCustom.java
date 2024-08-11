package org.example.ted.repositories;


import org.example.ted.models.Notification;
import org.example.ted.models.User;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<Notification> findByReceiver(User receiver);
}
