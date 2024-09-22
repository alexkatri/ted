package org.example.ted.controllers;

import org.example.ted.exceptions.RequestNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Notification;
import org.example.ted.models.Request;
import org.example.ted.models.User;
import org.example.ted.repositories.NotificationRepository;
import org.example.ted.repositories.RequestRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestRepository repository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    RequestController(RequestRepository repository, UserRepository userRepository, NotificationRepository notificationRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    // Aggregate root
    @GetMapping
    List<Request> all() {
        return repository.findAll();
    }

    // Single item
    @GetMapping("{id}")
    Request one(@PathVariable long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
    }

    // get user's received requests
    @GetMapping("/user/{receiverId}")
    List<Request> allByReceiver(@PathVariable(value = "receiverId") long receiverId) {
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new UserNotFoundException(receiverId));
        return repository.findByReceiver(receiver);
    }

    @PostMapping
    Request newRequest(@RequestBody Request newRequest) {
        // create new notification
        notificationRepository.save(new Notification(newRequest.getReceiver(), newRequest.getSender(), "request"));
        return repository.save(newRequest);
    }


    @PutMapping("{id}")
    Request replaceRequest(@RequestBody Request newRequest, @PathVariable long id) {

        return repository.findById(id)
                .map(request -> {
                    request.setReceiver(newRequest.getReceiver());
                    request.setSender(newRequest.getSender());
                    return repository.save(request);
                })
                .orElseGet(() -> {
                    newRequest.setId((int) id);
                    return repository.save(newRequest);
                });
    }

    @DeleteMapping("{id}")
    void deleteRequest(@PathVariable long id) {
        repository.deleteById(id);
    }
}
