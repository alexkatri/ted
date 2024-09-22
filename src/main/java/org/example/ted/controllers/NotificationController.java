package org.example.ted.controllers;

import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Comment;
import org.example.ted.models.Like;
import org.example.ted.models.Notification;
import org.example.ted.models.User;
import org.example.ted.repositories.NotificationRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository repository;
    private final UserRepository userRepository;

    @Autowired
    NotificationController(NotificationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // Aggregate root
    @GetMapping
    List<Notification> all() {
        return repository.findAll();
    }

    // Single item
    @GetMapping("{id}")
    Notification one(@PathVariable long id) {
        return repository.findById(id).orElse(null);
    }

    // get user's notifications
    @GetMapping("/user/{id}")
    List<Notification> allByReceiver(@PathVariable(value = "id") long id) {
        return repository.findByReceiverId(id);
    }

    // get user's notifications from received likes
    @GetMapping("/user/{id}/likes")
    List<Like> getNotificationLikes(@PathVariable(value = "id") long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        return repository.getNotificationLikes(user);
    }

    // get user's notifications from received comments
    @GetMapping("/user/{id}/comments")
    List<Comment> getNotificationComments(@PathVariable(value = "id") long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        return repository.getNotificationComments(user);
    }

    @PostMapping
    Notification newNotification(@RequestBody Notification newNotification) {
        return repository.save(newNotification);
    }

    @DeleteMapping("{id}")
    void deleteNotification(@PathVariable long id) {
        repository.deleteById(id);
    }
}
