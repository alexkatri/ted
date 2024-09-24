package org.example.ted.controllers;

import org.example.ted.models.User;
import org.example.ted.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userrepository;

    @Autowired
    public AdminController(UserRepository userrepository) {
        this.userrepository = userrepository;
    }

    @GetMapping
    String home() {
        return "Hello Admin";
    }

    @GetMapping("users")
    List<User> users() {
        return userrepository.findAll();
    }
}
