package org.example.ted.controllers;

import org.example.ted.exceptions.ArticleNotFoundException;
import org.example.ted.models.Article;
import org.example.ted.models.Comment;
import org.example.ted.models.Like;
import org.example.ted.models.User;
import org.example.ted.repositories.ArticleRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userrepository;

    public AdminController(UserRepository userrepository) {
        this.userrepository = userrepository;
    }

    // Aggregate root

    @GetMapping
    String home() {
        return "Hello Admin";
    }

    // Single item

    @GetMapping("users")
    List<User> users() {
        return userrepository.findAll();
    }
}
