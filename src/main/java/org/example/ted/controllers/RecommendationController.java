package org.example.ted.controllers;

import org.example.ted.models.*;
import org.example.ted.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // post new user-advertisement rating
    @PostMapping("/advertisement")
    ResponseEntity<UserAdvertisementRating> newUserAdvertisementRating(@RequestBody User user, @RequestBody Advertisement advertisement) {
        UserAdvertisementRatingId userAdvertisementRatingId = new UserAdvertisementRatingId();
        userAdvertisementRatingId.setUserId(user.getId());
        userAdvertisementRatingId.setAdvertisementId(advertisement.getId());
        UserAdvertisementRating rating = new UserAdvertisementRating();
        rating.setId(userAdvertisementRatingId);
        rating.setUser(user);
        rating.setAdvertisement(advertisement);
        rating.setValue(1);
        userAdvertisementRatingRepository.save(rating);
        return ResponseEntity.ok().body(rating);
    }

    // post new user-article rating
    @PostMapping("/article")
    ResponseEntity<UserArticleRating> newUserArticleRating(@RequestBody User user, @RequestBody Article article, int value) {
        UserArticleRatingId userArticleRatingId = new UserArticleRatingId();
        userArticleRatingId.setUserId(user.getId());
        userArticleRatingId.setArticleId(article.getId());
        UserArticleRating rating = new UserArticleRating();
        rating.setId(userArticleRatingId);
        rating.setUser(user);
        rating.setArticle(article);
        rating.setValue(value);
        userArticleRatingRepository.save(rating);
        return ResponseEntity.ok().body(rating);
    }

    @PutMapping("article")
    UserArticleRating replaceUserArticleRating(@RequestBody User user, @RequestBody Article article, int value) {
        UserArticleRating userArticleRating = userArticleRatingRepository.findByUserAndArticle(user, article);
        userArticleRating.setValue(value);
        return userArticleRatingRepository.save(userArticleRating);
    }

    @DeleteMapping("advertisement")
    void deleteUserAdvertisementRating(@RequestBody User user, @RequestBody Advertisement advertisement) {
        UserAdvertisementRating userAdvertisementRating = userAdvertisementRatingRepository
                .findByUserAndAdvertisement(user, advertisement);
        assert userAdvertisementRating != null;
        userAdvertisementRatingRepository.delete(userAdvertisementRating);
    }

    @DeleteMapping("article")
    void deleteUserArticleRating(@RequestBody User user, @RequestBody Article article) {
        UserArticleRating userArticleRating = userArticleRatingRepository.findByUserAndArticle(user, article);
        assert userArticleRating != null;
        userArticleRatingRepository.delete(userArticleRating);
    }
}
