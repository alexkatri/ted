package org.example.ted.controllers;

import org.example.ted.exceptions.ArticleNotFoundException;
import org.example.ted.models.Article;
import org.example.ted.repositories.ArticleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
