package org.example.ted.controllers;

import org.example.ted.models.Comment;
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

    @PostMapping
    Comment newComment(@RequestBody Comment newComment) {
        return repository.save(newComment);
    }

    // Single item

//    @GetMapping("{id}")
//    Profile one(@PathVariable long id) {
//
//        return repository.findById(id)
//                .orElseThrow(() -> new ProfileNotFoundException(id));
//    }


//    @PutMapping("{id}")
//    Comment replaceComment(@RequestBody Comment newProfile, @PathVariable long id) {
//
//        return repository.findById(id)
//                .map(profile -> {
//                    profile.setCreatedBy(newProfile.getCreatedBy());
//                    profile.setCv(newProfile.getCv());
//                    profile.setExperience(newProfile.getExperience());
//                    profile.setEducation(newProfile.getEducation());
//                    profile.setSkills(newProfile.getSkills());
//                    return repository.save(profile);
//                })
//                .orElseGet(() -> {
//                    newProfile.setId((int) id);
//                    return repository.save(newProfile);
//                });
//    }

    @DeleteMapping("{id}")
    void deleteComment(@PathVariable long id) {
        repository.deleteById(id);
    }
}
