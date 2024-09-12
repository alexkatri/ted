package org.example.ted.controllers;

import org.example.ted.exceptions.ArticleNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.*;
import org.example.ted.repositories.ArticleRepository;
import org.example.ted.repositories.UserRepository;
import org.example.ted.security.JWTGenerator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository repository;
    private UserRepository userRepository;
    private JWTGenerator JWTgenerator;

    private static String UPLOAD_FOLDER = System.getProperty("user.dir") + "/src/main/resources/temp";

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
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
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
        int userId = userEntity.getId();
        return repository.userFeed(userId);
    }

    @PostMapping
    Article newArticle(@RequestBody Article newArticle) {
        return repository.save(newArticle);
    }

    // multimedia upload
    @PostMapping("/multimedia_upload/{id}")
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

            Article article = repository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
            article.setMultimediaPath(path.toString());
            repository.save(article);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Successfully uploaded " + file.getOriginalFilename();
    }
    // Single item

    @GetMapping("{id}")
    Article one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    // get user image
    @GetMapping("/multimedia/{id}")
    public ResponseEntity<Resource> getArticleMultimedia(@PathVariable long id) throws IOException {
        Article article = repository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        Path multimediaPath = Paths.get(article.getMultimediaPath());
        Resource resource = new FileSystemResource(multimediaPath.toFile());
        String contentType = Files.probeContentType(multimediaPath);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
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
