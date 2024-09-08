package org.example.ted.controllers;

import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.User;
import org.example.ted.repositories.UserAdvertisementRatingRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class MatrixFactorizationController {

    private final UserAdvertisementRatingRepository userAdvertisementRatingRepository;

    public MatrixFactorizationController(UserAdvertisementRatingRepository userAdvertisementRatingRepository) {
        this.userAdvertisementRatingRepository = userAdvertisementRatingRepository;
    }

//    MatrixFactorizationController(UserRepository repository) {
//        this.repository = repository;
//    }

    // Aggregate root

    @GetMapping
    double[][] getMatrixFactorization() {
        return userAdvertisementRatingRepository.getMatrix(5);
    }
//
//    @PostMapping
//    User newUser(@RequestBody User newUser) {
//        return repository.save(newUser);
//    }
//
//    // Single item
//
//    @GetMapping("{id}")
//    User one(@PathVariable long id) {
//        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//    }
//
//
//    @PutMapping("{id}")
//    User replaceUser(@RequestBody User newUser, @PathVariable long id) {
//
//        return repository.findById(id)
//                .map(user -> {
//                    user.setEmail(newUser.getEmail());
//                    user.setPassword(newUser.getPassword());
//                    user.setName(newUser.getName());
//                    user.setSurname(newUser.getSurname());
//                    user.setPhoneNumber(newUser.getPhoneNumber());
//                    user.setRoles(newUser.getRoles());
//                    return repository.save(user);
//                })
//                .orElseThrow(() -> new UserNotFoundException(id));
////                .orElseGet(() -> {
////                    newUser.setEmail(user_email);
////                    return repository.save(newUser);
////                });
//    }
//
//    @DeleteMapping("{id}")
//    void deleteUser(@PathVariable long id) {
//        repository.deleteById((int) id);
//    }
}
