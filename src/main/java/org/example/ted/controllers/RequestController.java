package org.example.ted.controllers;

import org.example.ted.exceptions.RequestNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Notification;
import org.example.ted.models.Request;
import org.example.ted.models.User;
import org.example.ted.repositories.NotificationRepository;
import org.example.ted.repositories.RequestRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestRepository repository;
    private final UserRepository userRepository;
    private NotificationRepository notificationRepository;

    RequestController(RequestRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // Aggregate root

    @GetMapping
    List<Request> all() {
        return repository.findAll();
    }

    @PostMapping
    Request newRequest(@RequestBody Request newRequest) {
        notificationRepository.save(new Notification(newRequest.getReceiver(), newRequest.getSender(), "request"));
        return repository.save(newRequest);
    }

    // Single item

    @GetMapping("{id}")
    Request one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
    }

    @GetMapping("/user/{receiverId}")
    List<Request> allByReceiver(@PathVariable(value = "receiverId") long receiverId) {
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new UserNotFoundException(receiverId));
        return repository.findByReceiver(receiver);
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
    void deleteRequest(@PathVariable long id) {
        repository.deleteById(id);
    }
}
