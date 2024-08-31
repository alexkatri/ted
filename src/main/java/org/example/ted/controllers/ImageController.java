package org.example.ted.controllers;

import org.example.ted.exceptions.ImageNotFoundException;
import org.example.ted.exceptions.UserNotFoundException;
import org.example.ted.models.Image;
import org.example.ted.models.User;
import org.example.ted.repositories.ImageRepository;
import org.example.ted.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/images")
public
class ImageController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C://Users//sgt_o//IdeaProjects//ted//src//main//resources//templates";

    private final ImageRepository repository;
    private final UserRepository userRepository;

    ImageController(ImageRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @GetMapping("uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @GetMapping
    List<Image> all() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    Image one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

    @GetMapping("/user/{id}")
    Image byUser(@PathVariable(value = "id") long id) {
        User user = userRepository.findById(id).get();
        return repository.findByUser(user);
    }

    @PostMapping("upload")
    public String singleFileUpload(@RequestParam("picture") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/images/uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER
                    + file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf('/'), file.getOriginalFilename().lastIndexOf('.'))
                    + new Random().nextInt(1 << 20)
                    + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
            );
            Files.write(path, bytes);
            Image img = new Image(path.toString());
            repository.save(img);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/images/uploadStatus";
    }

    @PostMapping
    Image newImage(@RequestBody Image newImage) {
        return repository.save(newImage);
    }

    @PutMapping("{id}")
    Image replaceImage(@RequestBody Image newImage, @PathVariable Long id) {

        return repository.findById(id)
                .map(Image -> {
                    Image.setPath(newImage.getPath());
                    return repository.save(Image);
                })
                .orElseGet(() -> {
                    newImage.setId(Math.toIntExact(id));
                    return repository.save(newImage);
                });
    }

    @DeleteMapping("{id}")
    void deleteImage(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
