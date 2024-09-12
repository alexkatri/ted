package org.example.ted.controllers;

import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.User;
import org.example.ted.repositories.UserRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    private static String UPLOAD_FOLDER = System.getProperty("user.dir") + "/src/main/resources/temp";

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping
    List<User> all() {
        return repository.findAll();
    }

    @PostMapping
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // image upload
    @PostMapping("/image_upload/{id}")
    public String singleFileUpload(@PathVariable long id, @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER
                    + "/"
                    + file.getOriginalFilename()
                    .substring(0, file.getOriginalFilename().lastIndexOf('.'))
                    + new Random().nextInt(1 << 20)
                    + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
            );
//            Path path = Paths.get(UPLOAD_FOLDER + "/"
//                    + file.getOriginalFilename()
//            );
            Files.write(path, bytes);

            User user = repository.findById(id).get();
            user.setImagePath(path.toString());
            repository.save(user);

//            redirectAttributes.addFlashAttribute("message",
//                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Successfully uploaded " + file.getOriginalFilename();
    }

    // Single item

    @GetMapping("{id}")
    User one(@PathVariable long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    // get user image
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getUserImage(@PathVariable long id) throws IOException {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Path imagePath = Paths.get(user.getImagePath());
        Resource resource = new FileSystemResource(imagePath.toFile());
        String contentType = Files.probeContentType(imagePath);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    @PutMapping("{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable long id) {

        return repository.findById(id)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    user.setName(newUser.getName());
                    user.setSurname(newUser.getSurname());
                    user.setPhoneNumber(newUser.getPhoneNumber());
                    user.setRoles(newUser.getRoles());
                    return repository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
//                .orElseGet(() -> {
//                    newUser.setEmail(user_email);
//                    return repository.save(newUser);
//                });
    }

    @DeleteMapping("{id}")
    void deleteUser(@PathVariable long id) {
        repository.deleteById((int) id);
    }
}
