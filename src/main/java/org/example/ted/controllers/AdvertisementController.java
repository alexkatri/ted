package org.example.ted.controllers;

import org.example.ted.exceptions.AdvertisementNotFoundException;
import org.example.ted.models.Advertisement;
import org.example.ted.models.Notification;
import org.example.ted.models.User;
import org.example.ted.models.UserAdvertisementMatrix;
import org.example.ted.repositories.AdvertisementRepository;
import org.example.ted.repositories.NotificationRepository;
import org.example.ted.repositories.UserAdvertisementMatrixRepository;
import org.example.ted.repositories.UserRepository;
import org.example.ted.security.JWTGenerator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/advertisements")
public class AdvertisementController {

    private AdvertisementRepository repository;
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private UserAdvertisementMatrixRepository userAdvertisementMatrixRepository;
    private JWTGenerator JWTgenerator;

    AdvertisementController(AdvertisementRepository repository, NotificationRepository notificationRepository, UserRepository userRepository, UserAdvertisementMatrixRepository userAdvertisementMatrixRepository, JWTGenerator jwTgenerator) {
        this.repository = repository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.userAdvertisementMatrixRepository = userAdvertisementMatrixRepository;
        JWTgenerator = jwTgenerator;
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

    @GetMapping("/show")
    List<Advertisement> showAdvertisements(@RequestHeader(name = "Authorization") String token) {
        token = token.substring(7);
        if (!JWTgenerator.validateToken(token)) {
            return null;
        }
        String user_email = JWTgenerator.getEmailFromJWT(token);
        Optional<User> user = userRepository.findByEmail(user_email);
        return repository.showAdvertisements(user);
    }

    @PostMapping
    Advertisement newAdvertisement(@RequestBody Advertisement newAdvertisement) {
        for (int i = 1; i <= userRepository.count(); i++) {
            userAdvertisementMatrixRepository.save(new UserAdvertisementMatrix(userRepository.findById(i).get(), newAdvertisement));
        }
        return repository.save(newAdvertisement);
    }

    @PostMapping("/{id}/requests")
    Advertisement requestAdvertisement(@PathVariable(value = "id") Long id,
                              @RequestBody User requestedBy) {
        repository.findById(id).ifPresent(advertisement -> {
            Set<User> request_users = advertisement.getRequest_users();
            request_users.add(requestedBy);
            advertisement.setRequest_users(request_users);
            repository.save(advertisement);
            notificationRepository.save(new Notification(advertisement.getCreatedBy(), requestedBy,
                    "new advertisement request"));
        });
        return repository.findById(id).get();
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
