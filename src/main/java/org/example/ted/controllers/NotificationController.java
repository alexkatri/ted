package org.example.ted.controllers;

import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Comment;
import org.example.ted.models.Like;
import org.example.ted.models.Notification;
import org.example.ted.models.User;
import org.example.ted.repositories.NotificationRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository repository;
    private final UserRepository userRepository;

    NotificationController(NotificationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // Aggregate root

    @GetMapping
    List<Notification> all() {
        return repository.findAll();
    }

    @GetMapping("/user/{id}")
    List<Notification> allByReceiver(@PathVariable(value = "receiver") long id) {
        return repository.findByReceiverId(id);
    }

    @GetMapping("/user/{id}/likes")
    List<Like> getNotificationLikes(@PathVariable(value = "id") long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        return repository.getNotificationLikes(user);
    }

    @GetMapping("/user/{id}/comments")
    List<Comment> getNotificationComments(@PathVariable(value = "id") long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        return repository.getNotificationComments(user);
    }

    @PostMapping
    Notification newNotification(@RequestBody Notification newNotification) {
        return repository.save(newNotification);
    }

    // Single item

//    @GetMapping("{id}")
//    Notification one(@PathVariable long id) {
//
//        return repository.findById(id);
//                .orElseThrow(() -> new ProfileNotFoundException(id));
//    }

//    @PutMapping("{id}")
//    Comment replaceComment(@RequestBody Comment newProfile, @PathVariable long id) {
//
//        return repository.findById(id)
//                .map(profile -> {
//                    profile.setCreatedBy(newProfile.getCreatedBy());
//                    profile.setCv(newProfile.getCv());
//                    profile.setExperience(newProfile.getExperience());
//                    profile.setEducation(newProfile.getEducation());
//                    profile.setSkills(newProfile.getSkills());
//                    return repository.save(profile);
//                })
//                .orElseGet(() -> {
//                    newProfile.setId((int) id);
//                    return repository.save(newProfile);
//                });
//    }

    @DeleteMapping("{id}")
    void deleteNotification(@PathVariable long id) {
        repository.deleteById(id);
    }
}
