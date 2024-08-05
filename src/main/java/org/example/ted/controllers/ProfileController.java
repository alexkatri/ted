package org.example.ted.controllers;

import org.example.ted.exceptions.ProfileNotFoundException;
import org.example.ted.models.Profile;
import org.example.ted.repositories.ProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileRepository repository;

    ProfileController(ProfileRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping
    List<Profile> all() {
        return repository.findAll();
    }

    @PostMapping
    Profile newProfile(@RequestBody Profile newProfile) {
        return repository.save(newProfile);
    }

    // Single item

    @GetMapping("{id}")
    Profile one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(id));
    }


    @PutMapping("{id}")
    Profile replaceProfile(@RequestBody Profile newProfile, @PathVariable long id) {

        return repository.findById(id)
                .map(profile -> {
                    profile.setCreatedBy(newProfile.getCreatedBy());
                    profile.setCv(newProfile.getCv());
                    profile.setExperience(newProfile.getExperience());
                    profile.setEducation(newProfile.getEducation());
                    profile.setSkills(newProfile.getSkills());
                    return repository.save(profile);
                })
                .orElseGet(() -> {
                    newProfile.setId((int) id);
                    return repository.save(newProfile);
                });
    }

    @DeleteMapping("{id}")
    void deleteAdvertisement(@PathVariable long id) {
        repository.deleteById(id);
    }
}
