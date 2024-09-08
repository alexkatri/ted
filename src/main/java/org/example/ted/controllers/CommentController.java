package org.example.ted.controllers;

import org.example.ted.exceptions.ArticleNotFoundException;
import org.example.ted.exceptions.CommentNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Article;
import org.example.ted.models.Comment;
import org.example.ted.models.Notification;
import org.example.ted.models.User;
import org.example.ted.repositories.ArticleRepository;
import org.example.ted.repositories.CommentRepository;
import org.example.ted.repositories.NotificationRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private NotificationRepository notificationRepository;

    CommentController(CommentRepository repository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    // Aggregate root

    @GetMapping
    List<Comment> all() {
        return repository.findAll();
    }

    @GetMapping("/user/{id}")
    List<Comment> allByUser(@PathVariable(value = "id") long id) {
        User createdBy = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return repository.findByCreatedBy(createdBy);
    }

    @GetMapping("/article/{id}")
    List<Comment> allByArticle(@PathVariable(value = "id") long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        return repository.findByArticle(article);
    }

    @PostMapping
    Comment newComment(@RequestBody Comment newComment) {
        notificationRepository.save(new Notification(newComment.getArticle().getCreatedBy(), newComment.getCreatedBy(), "" +
                "comment"));
        return repository.save(newComment);
    }

    // Single item

    @GetMapping("{id}")
    Comment one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
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
                .orElseGet(() -> {
                    newComment.setId((int) id);
                    return repository.save(newComment);
                });
    }

    @DeleteMapping("{id}")
    void deleteComment(@PathVariable long id) {
        repository.deleteById(id);
    }
}
