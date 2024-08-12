package org.example.ted.controllers;

import org.example.ted.exceptions.CommentNotFoundException;
import org.example.ted.models.Comment;
import org.example.ted.models.User;
import org.example.ted.repositories.CommentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentRepository repository;

    CommentController(CommentRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping
    List<Comment> all() {
        return repository.findAll();
    }

    @GetMapping("/user/{created_by}")
    List<Comment> allByUser(@PathVariable(value = "created_by") User createdBy) {
        return repository.findByUser(createdBy);
    }

    @PostMapping
    Comment newComment(@RequestBody Comment newComment) {
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
