package org.example.ted.controllers;

import org.example.ted.exceptions.ProfileNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Profile;
import org.example.ted.models.User;
import org.example.ted.repositories.ProfileRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileRepository repository;
    private final UserRepository userRepository;

    @Autowired
    ProfileController(ProfileRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // Aggregate root
    @GetMapping
    List<Profile> all() {
        return repository.findAll();
    }

    // Single item
    @GetMapping("{id}")
    Profile one(@PathVariable long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(id));
    }

    // get user's profile
    @GetMapping("/user/{id}")
    Profile byUser(@PathVariable(value = "id") long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return repository.findByCreatedBy(user);
    }

    @PostMapping
    Profile newProfile(@RequestBody Profile newProfile) {
        return repository.save(newProfile);
    }

    @PutMapping("{id}")
    Profile replaceProfile(@RequestBody Profile newProfile, @PathVariable long id) {

        return repository.findById(id)
                .map(profile -> {
                    profile.setCreatedBy(newProfile.getCreatedBy());
                    profile.setCv(newProfile.getCv());
                    profile.setExperience(newProfile.getExperience());
                    profile.setEducation(newProfile.getEducation());
                    return repository.save(profile);
                })
                .orElseGet(() -> {
                    newProfile.setId((int) id);
                    return repository.save(newProfile);
                });
    }

    @DeleteMapping("{id}")
    void deleteProfile(@PathVariable long id) {
        repository.deleteById(id);
    }
}
