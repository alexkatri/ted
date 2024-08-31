package org.example.ted.controllers;

import org.example.ted.exceptions.AdvertisementNotFoundException;
import org.example.ted.models.Advertisement;
import org.example.ted.models.Notification;
import org.example.ted.models.User;
import org.example.ted.models.UserAdvertisementRating;
import org.example.ted.repositories.AdvertisementRepository;
import org.example.ted.repositories.NotificationRepository;
import org.example.ted.repositories.UserAdvertisementRatingRepository;
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
    private UserAdvertisementRatingRepository userAdvertisementRatingRepository;
    private JWTGenerator JWTgenerator;

    AdvertisementController(AdvertisementRepository repository, NotificationRepository notificationRepository, UserRepository userRepository, UserAdvertisementRatingRepository userAdvertisementRatingRepository, JWTGenerator jwTgenerator) {
        this.repository = repository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.userAdvertisementRatingRepository = userAdvertisementRatingRepository;
        JWTgenerator = jwTgenerator;
    }

    // Aggregate root

    @GetMapping
    List<Advertisement> all() {
        return repository.findAll();
    }

    @GetMapping("/user/{id}")
    List<Advertisement> allByUser(@PathVariable(value = "id") long id) {
        User createdBy = userRepository.findById(id).get();
        return repository.findByCreatedBy(createdBy);
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

//    @GetMapping("{id}")
//    Advertisement one(@PathVariable long id) {
//
//        return repository.findById(id)
//                .orElseThrow(() -> new AdvertisementNotFoundException(id));
//    }

    @GetMapping("/{id}")
    Advertisement byId(@PathVariable long id, @RequestHeader(name = "Authorization") String token) {
        token = token.substring(7);
        if (!JWTgenerator.validateToken(token)) {
            return null;
        }
        String user_email = JWTgenerator.getEmailFromJWT(token);
        Optional<User> user = userRepository.findByEmail(user_email);
        if (!user.isPresent()) {
            return null;
        }
        User userEntity = user.get();
        if (repository.existsById(id)) {
            userAdvertisementRatingRepository.save(new UserAdvertisementRating(userEntity, repository.findById(id).get(), 1, "known"));
        }
        return repository.findById(id).orElseThrow(() -> new AdvertisementNotFoundException(id));
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
