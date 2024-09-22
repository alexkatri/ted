package org.example.ted.controllers;

import org.example.ted.exceptions.ArticleNotFoundException;
import org.example.ted.exceptions.CommentNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.*;
import org.example.ted.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final NotificationRepository notificationRepository;
    private final UserArticleRatingRepository userArticleRatingRepository;

    @Autowired
    CommentController(CommentRepository repository, UserRepository userRepository, ArticleRepository articleRepository, NotificationRepository notificationRepository, UserArticleRatingRepository userArticleRatingRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.notificationRepository = notificationRepository;
        this.userArticleRatingRepository = userArticleRatingRepository;
    }

    // Aggregate root
    @GetMapping
    List<Comment> all() {
        return repository.findAll();
    }

    // Single item
    @GetMapping("{id}")
    Comment one(@PathVariable long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    // get comments created by user
    @GetMapping("/user/{id}")
    List<Comment> allByUser(@PathVariable(value = "id") long id) {
        User createdBy = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return repository.findByCreatedBy(createdBy);
    }

    // get article's comments
    @GetMapping("/article/{id}")
    List<Comment> allByArticle(@PathVariable(value = "id") long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        return repository.findByArticle(article);
    }

    @PostMapping
    Comment newComment(@RequestBody Comment newComment) {
        notificationRepository.save(new Notification(newComment.getArticle().getCreatedBy(), newComment.getCreatedBy(),
                "comment"));
        Article article = newComment.getArticle();
        User user = newComment.getCreatedBy();
        // update article rating
        if (!userArticleRatingRepository.existsByUserAndArticle(user, article)) {
            UserArticleRatingId userArticleRatingId = new UserArticleRatingId(user.getId(), article.getId());
            userArticleRatingRepository.save(new UserArticleRating(userArticleRatingId, user, article, 2));
        }
        else {
            UserArticleRating userArticleRating = userArticleRatingRepository.findByUserAndArticle(user, article);
            int value = userArticleRating.getValue();
            userArticleRating.setValue(value + 3);
            userArticleRatingRepository.save(userArticleRating);
        }
        return repository.save(newComment);
    }

    @PutMapping("{id}")
    Comment replaceComment(@RequestBody Comment newComment, @PathVariable long id) {

        return repository.findById(id)
                .map(comment -> {
                    comment.setCreatedBy(newComment.getCreatedBy());
                    comment.setArticle(newComment.getArticle());
                    comment.setText(newComment.getText());
                    return repository.save(comment);
                })
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    @DeleteMapping("{id}")
    void deleteComment(@PathVariable long id) {
        repository.deleteById(id);
    }
}
