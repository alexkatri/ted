package org.example.ted.controllers;

import org.example.ted.models.Advertisement;
import org.example.ted.models.User;
import org.example.ted.repositories.AdvertisementRepository;
import org.example.ted.repositories.UserAdvertisementRatingRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    UserRepository userRepository;
    AdvertisementRepository advertisementRepository;
    UserAdvertisementRatingRepository userAdvertisementRatingRepository;

    @Autowired
    public RecommendationController(UserRepository userRepository, AdvertisementRepository advertisementRepository,
            UserAdvertisementRatingRepository userAdvertisementRatingRepository) {
        this.userAdvertisementRatingRepository = userAdvertisementRatingRepository;
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
    }

    // Aggregate root

    @GetMapping
    double[][] getMatrixFactorization() {
        return userAdvertisementRatingRepository.getMatrix(5);
    }

    @GetMapping("/user/{id}")
    List<Advertisement> getUserRecommendedAdvertisements(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return userAdvertisementRatingRepository.getRecommendedAdvertisementsByUser(user);
    }

//    @PostMapping("/initialize")
//    void initialize() {
//        for (User user : userRepository.findAll()) {
//            for (Advertisement advertisement : advertisementRepository.findAll()) {
//                UserAdvertisementRatingId userAdvertisementRatingId = new UserAdvertisementRatingId(user.getId(), advertisement.getId());
//                userAdvertisementRatingRepository.save(new UserAdvertisementRating(userAdvertisementRatingId, user, advertisement, 0, "unknown"));
//            }
//        }
//    }

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
