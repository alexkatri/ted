package org.example.ted.controllers;

import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Role;
import org.example.ted.models.User;
import org.example.ted.repositories.RoleRepository;
import org.example.ted.repositories.UserRepository;
import org.example.ted.security.JWTGenerator;
import org.example.ted.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;
    private final JWTGenerator JWTgenerator;

    private static final String UPLOAD_FOLDER = System.getProperty("user.dir") + "/src/main/resources/temp";
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RoleRepository roleRepository;

    @Autowired
    UserController(UserRepository repository, JWTGenerator jwTgenerator, UserDetailsServiceImpl userDetailsServiceImpl, RoleRepository roleRepository) {
        this.repository = repository;
        JWTgenerator = jwTgenerator;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.roleRepository = roleRepository;
    }

    // Aggregate root
    @GetMapping
    public ResponseEntity<List<User>> all() {
//        token = token.substring(7);
//        // check token validity
//        if (!JWTgenerator.validateToken(token)) {
//            return ResponseEntity.badRequest().build();
//        }
//        String user_email = JWTgenerator.getEmailFromJWT(token);
//        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user_email);
//        if (userDetails == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        // if user's role is ADMIN
//        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
//            return ResponseEntity.ok(repository.findAll());
//        }
//        return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repository.findAll());
    }

    // Single item
    @GetMapping("{id}")
    User one(@PathVariable long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    // get user's image
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getUserImage(@PathVariable long id) throws IOException {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Path imagePath = Paths.get(user.getImagePath());
        Resource resource = new FileSystemResource(imagePath.toFile());
        String contentType = Files.probeContentType(imagePath);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    @PostMapping
    User newUser(@RequestBody User newUser) {
        // default profile photo
        Path path = Paths.get(UPLOAD_FOLDER + "/" + "userPhoto.jpg");
        newUser.setImagePath(path.toString());
        // default role is USER
        Role role = roleRepository.findByName("USER").get();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        newUser.setRoles(roles);
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
            Files.write(path, bytes);

            User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
            user.setImagePath(path.toString());
            repository.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Successfully uploaded " + file.getOriginalFilename();
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
    }

    @DeleteMapping("{id}")
    void deleteUser(@PathVariable long id) {
        repository.deleteById((int) id);
    }
}
