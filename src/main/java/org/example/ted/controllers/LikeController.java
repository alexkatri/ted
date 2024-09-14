package org.example.ted.controllers;

import org.example.ted.exceptions.ArticleNotFoundException;
import org.example.ted.exceptions.LikeNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.*;
import org.example.ted.repositories.*;
import org.example.ted.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final LikeRepository repository;
    private final NotificationRepository notificationRepository;
    private final UserArticleRatingRepository userArticleRatingRepository;

    @Autowired
    LikeController(LikeRepository repository, UserRepository userRepository, ArticleRepository articleRepository, NotificationRepository notificationRepository, UserArticleRatingRepository userArticleRatingRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.notificationRepository = notificationRepository;
        this.userArticleRatingRepository = userArticleRatingRepository;
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

    @GetMapping("/article/{id}")
    List<Like> allByArticle(@PathVariable(value = "id") long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        return repository.findByArticle(article);
    }

    @PostMapping
    Like newLike(@RequestBody Like newLike) {
        notificationRepository.save(new Notification(newLike.getArticle().getCreatedBy(), newLike.getCreatedBy(),
                "like"));
        Article article = newLike.getArticle();
        User user = newLike.getCreatedBy();
        if (!userArticleRatingRepository.existsByUserAndArticle(user, article)) {
            UserArticleRatingId userArticleRatingId = new UserArticleRatingId(user.getId(), article.getId());
            userArticleRatingRepository.save(new UserArticleRating(userArticleRatingId, user, article, 2));
        }
        else {
            UserArticleRating userArticleRating = userArticleRatingRepository.findByUserAndArticle(user, article);
            int value = userArticleRating.getValue();
            userArticleRating.setValue(value + 2);
            userArticleRatingRepository.save(userArticleRating);
        }
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
