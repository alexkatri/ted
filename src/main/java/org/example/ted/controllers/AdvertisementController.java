package org.example.ted.controllers;

import org.example.ted.exceptions.AdvertisementNotFoundException;
import org.example.ted.models.Advertisement;
import org.example.ted.models.User;
import org.example.ted.repositories.AdvertisementRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advertisements")
public class AdvertisementController {

    private AdvertisementRepository repository;

    AdvertisementController(AdvertisementRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping
    List<Advertisement> all() {
        return repository.findAll();
    }

    @GetMapping("/user/{created_by}")
    List<Advertisement> allByUser(@PathVariable(value = "created_by") User createdBy) {
        return repository.findByUser(createdBy);
    }

    @PostMapping
    Advertisement newAdvertisement(@RequestBody Advertisement newAdvertisement) {
        return repository.save(newAdvertisement);
    }

    // Single item

    @GetMapping("{id}")
    Advertisement one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new AdvertisementNotFoundException(id));
    }


    @PutMapping("{id}")
    Advertisement replaceAdvertisement(@RequestBody Advertisement newAdvertisement, @PathVariable long id) {
        return repository.findById(id)
                .map(advertisement -> {
                    advertisement.setCreatedBy(newAdvertisement.getCreatedBy());
                    advertisement.setRequest_users(newAdvertisement.getRequest_users());
                    advertisement.setText(newAdvertisement.getText());
                    return repository.save(advertisement);
                })
                .orElseGet(() -> {
                    newAdvertisement.setId((int) id);
                    return repository.save(newAdvertisement);
                });
    }

    @DeleteMapping("{id}")
    void deleteAdvertisement(@PathVariable long id) {
        repository.deleteById(id);
    }
}
