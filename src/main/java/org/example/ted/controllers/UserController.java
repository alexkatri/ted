package org.example.ted.controllers;

import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.User;
import org.example.ted.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping
    List<User> all() {
        return repository.findAll();
    }

    @PostMapping
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("{user_email}")
    User one(@PathVariable String user_email) {

        return repository.findByEmail(user_email)
                .orElseThrow(() -> new UserNotFoundException(user_email));
    }


    @PutMapping("{user_email}")
    User replaceUser(@RequestBody User newUser, @PathVariable String user_email) {

        return repository.findByEmail(user_email)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    user.setName(newUser.getName());
                    user.setSurname(newUser.getSurname());
                    user.setPhoneNumber(newUser.getPhoneNumber());
                    user.setRoles(newUser.getRoles());
                    return repository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(user_email));
//                .orElseGet(() -> {
//                    newUser.setEmail(user_email);
//                    return repository.save(newUser);
//                });
    }

    @DeleteMapping("{user_email}")
    void deleteUser(@PathVariable String user_email) {
        repository.deleteById(user_email);
    }
}
