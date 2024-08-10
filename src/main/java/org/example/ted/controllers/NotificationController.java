package org.example.ted.controllers;

import org.example.ted.exceptions.ProfileNotFoundException;
import org.example.ted.models.Notification;
import org.example.ted.models.User;
import org.example.ted.repositories.NotificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository repository;

    NotificationController(NotificationRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping
    List<Notification> all() {
        return repository.findAll();
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

    @GetMapping("/user/{user}")
    List<Notification> getAllByUser(@PathVariable(value = "user") User user) {
        return repository.findByUser(user);
    }

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
