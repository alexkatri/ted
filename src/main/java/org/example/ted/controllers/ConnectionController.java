package org.example.ted.controllers;

import org.example.ted.exceptions.ConnectionNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Connection;
import org.example.ted.models.User;
import org.example.ted.repositories.ConnectionRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/connections")
public class ConnectionController {

    private final ConnectionRepository repository;
    private final UserRepository userRepository;

    @Autowired
    ConnectionController(ConnectionRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // Aggregate root
    @GetMapping
    List<Connection> all() {
        return repository.findAll();
    }

    @GetMapping("/user/{id}")
    List<Connection> allByUser(@PathVariable(value = "id") long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return repository.findByUser(user);
    }

    // Single item
    @GetMapping("{id}")
    Connection one(@PathVariable long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ConnectionNotFoundException(id));
    }

    @PostMapping
    Connection newConnection(@RequestBody Connection newConnection) {
        return repository.save(newConnection);
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
    void deleteConnection(@PathVariable long id) {
        repository.deleteById(id);
    }
}
