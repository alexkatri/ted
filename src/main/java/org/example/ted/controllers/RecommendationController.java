package org.example.ted.controllers;

import org.example.ted.models.Advertisement;
import org.example.ted.models.Article;
import org.example.ted.models.User;
import org.example.ted.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final UserArticleRatingRepository userArticleRatingRepository;
    private final UserRepository userRepository;
    private final UserAdvertisementRatingRepository userAdvertisementRatingRepository;

    @Autowired
    public RecommendationController(UserRepository userRepository, UserAdvertisementRatingRepository userAdvertisementRatingRepository,
                                    UserArticleRatingRepository userArticleRatingRepository) {
        this.userAdvertisementRatingRepository = userAdvertisementRatingRepository;
        this.userRepository = userRepository;
        this.userArticleRatingRepository = userArticleRatingRepository;
    }

    // get user-advertisement rating factorized matrix
    @GetMapping("/advertisement")
    double[][] getUserAdvertisementMatrix() {
        return userAdvertisementRatingRepository.getMatrix();
    }

    // get user's recommended advertisements
    @GetMapping("/advertisement/user/{id}")
    List<Advertisement> getUserRecommendedAdvertisements(@PathVariable long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        if (user == null) {
            return null;
        }
        return userAdvertisementRatingRepository.getRecommendedAdvertisementsByUser(user);
    }

    // get user-article rating factorized matrix
    @GetMapping("/article")
    double[][] getMatrixFactorization() {
        return userArticleRatingRepository.getMatrix();
    }

    // get user's recommended articles
    @GetMapping("/article/user/{id}")
    List<Article> getUserRecommendedArticles(@PathVariable long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        if (user == null) {
            return null;
        }
        return userArticleRatingRepository.getRecommendedArticlesByUser(user);
    }

//    @PostMapping
//    User newUser(@RequestBody User newUser) {
//        return repository.save(newUser);
//    }
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
