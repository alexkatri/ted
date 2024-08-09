package org.example.ted.controllers;

import org.example.ted.exceptions.ArticleNotFoundException;
import org.example.ted.models.*;
import org.example.ted.repositories.ArticleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository repository;

    ArticleController(ArticleRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping
    List<Article> all() {
        return repository.findAll();
    }

    @PostMapping
    Article newArticle(@RequestBody Article newArticle) {
        return repository.save(newArticle);
    }

    // Single item

    @GetMapping("{id}")
    Article one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @GetMapping("/user/{user_email}")
    List<Article> user(@PathVariable(value = "user_email") User user_email) {

        return repository.findByUser(user_email);
    }

    @GetMapping("{id}/likes")
    Set<Like> articleLikes(@PathVariable long id) {

        Article article = repository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        return article.getLikes();
    }

    @GetMapping("{id}/comments")
    Set<Comment> articleComments(@PathVariable long id) {

        Article article = repository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        return article.getComments();
    }


    @PutMapping("{id}")
    Article replaceArticle(@RequestBody Article newArticle, @PathVariable long id) {

        return repository.findById(id)
                .map(article -> {
                    article.setCreatedBy(newArticle.getCreatedBy());
                    article.setDate(newArticle.getDate());
                    article.setViews(newArticle.getViews());
                    article.setMultimediaPath(newArticle.getMultimediaPath());
                    return repository.save(article);
                })
                .orElseGet(() -> {
                    newArticle.setId((int) id);
                    return repository.save(newArticle);
                });
    }

    @DeleteMapping("{id}")
    void deleteArticle(@PathVariable long id) {
        repository.deleteById(id);
    }
}
