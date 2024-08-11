package org.example.ted.controllers;

import org.example.ted.exceptions.RequestNotFoundException;
import org.example.ted.models.Request;
import org.example.ted.models.User;
import org.example.ted.repositories.RequestRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestRepository repository;

    RequestController(RequestRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping
    List<Request> all() {
        return repository.findAll();
    }

    @PostMapping
    Request newRequest(@RequestBody Request newRequest) {
        return repository.save(newRequest);
    }

    // Single item

    @GetMapping("{id}")
    Request one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
    }

    @GetMapping("/user/{receiver}")
    List<Request> allByReceiver(@PathVariable(value = "receiver") User receiver) {
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
