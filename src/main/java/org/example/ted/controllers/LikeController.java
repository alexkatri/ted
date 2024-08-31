package org.example.ted.controllers;

import org.example.ted.exceptions.AdvertisementNotFoundException;
import org.example.ted.exceptions.LikeNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Advertisement;
import org.example.ted.models.Like;
import org.example.ted.models.Notification;
import org.example.ted.models.User;
import org.example.ted.repositories.AdvertisementRepository;
import org.example.ted.repositories.LikeRepository;
import org.example.ted.repositories.NotificationRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final UserRepository userRepository;
    private LikeRepository repository;
    private NotificationRepository notificationRepository;

    LikeController(LikeRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // Aggregate root

    @GetMapping
    List<Like> all() {
        return repository.findAll();
    }

    @GetMapping("/user/{id}")
    List<Like> allByUser(@PathVariable(value = "id") long id) {
        User createdBy = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return repository.findByCreatedBy(createdBy);
    }

    @PostMapping
    Like newLike(@RequestBody Like newLike) {
        notificationRepository.save(new Notification(newLike.getArticle().getCreatedBy(), newLike.getCreatedBy(),
                "new like"));
        return repository.save(newLike);
    }

    // Single item

    @GetMapping("{id}")
    Like one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new LikeNotFoundException(id));
    }


    @PutMapping("{id}")
    Like replaceLike(@RequestBody Like newLike, @PathVariable long id) {
        return repository.findById(id)
                .map(like -> {
                    like.setArticle(newLike.getArticle());
                    like.setCreatedBy(newLike.getCreatedBy());
                    return repository.save(like);
                })
                .orElseGet(() -> {
                    newLike.setId((int) id);
                    return repository.save(newLike);
                });
    }

    @DeleteMapping("{id}")
    void deleteLike(@PathVariable long id) {
        repository.deleteById(id);
    }
}
