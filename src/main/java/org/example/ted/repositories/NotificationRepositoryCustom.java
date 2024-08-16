package org.example.ted.repositories;


import org.example.ted.models.Comment;
import org.example.ted.models.Like;
import org.example.ted.models.Notification;
import org.example.ted.models.User;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<Notification> findByReceiver(User receiver);
    List<Like> getNotificationLikes(User user_email);
    List<Comment> getNotificationComments(User user_email);
}
