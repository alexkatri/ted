package org.example.ted.controllers;

import org.example.ted.exceptions.ArticleNotFoundException;
import org.example.ted.models.*;
import org.example.ted.repositories.ArticleRepository;
import org.example.ted.repositories.UserRepository;
import org.example.ted.security.JWTGenerator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository repository;
    private UserRepository userRepository;
    private JWTGenerator JWTgenerator;

    ArticleController(ArticleRepository repository, UserRepository userRepository, JWTGenerator jwTgenerator) {
        this.repository = repository;
        this.userRepository = userRepository;
        JWTgenerator = jwTgenerator;
    }

    // Aggregate root

    @GetMapping
    List<Article> all() {
        return repository.findAll().stream().sorted().toList();
    }

    @GetMapping("/user/{id}")
    List<Article> allByUser(@PathVariable(value = "id") long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        return repository.findByCreatedBy(user).stream().sorted().toList();
    }

    @GetMapping("/feed")
    List<Article> allByUserFeed(@RequestHeader(name = "Authorization") String token) {
        token = token.substring(7);
        if (!JWTgenerator.validateToken(token)) {
            return null;
        }
        String user_email = JWTgenerator.getEmailFromJWT(token);
        Optional<User> user = userRepository.findByEmail(user_email);
        User userEntity = user.get();
        return repository.userFeed(userEntity);
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

    @GetMapping("{id}/likes")
    Set<Like> getLikes(@PathVariable long id) {

        Article article = repository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        return article.getLikes();
    }

    @GetMapping("{id}/comments")
    Set<Comment> getComments(@PathVariable long id) {

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
