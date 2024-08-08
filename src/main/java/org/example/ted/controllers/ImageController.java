package org.example.ted.controllers;

import org.example.ted.exceptions.ImageNotFoundException;
import org.example.ted.models.Image;
import org.example.ted.repositories.ImageRepository;
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
class ImageController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C://Users//sgt_o//IdeaProjects//ted//src//main//resources//templates";

    private final ImageRepository repository;

    ImageController(ImageRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

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

    @GetMapping("uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @GetMapping
    List<Image> all() {
        return repository.findAll();
    }

//    @PostMapping
//    Image newImage(@RequestBody Image newImage) {
//        return repository.save(newImage);
//    }

    // Single item

    @GetMapping("{id}")
    Image one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }


    @PutMapping("{id}")
    Image replaceImage(@RequestBody Image newImage, @PathVariable long id) {

        return repository.findById(id)
                .map(Image -> {
                    Image.setPath(newImage.getPath());
                    return repository.save(Image);
                })
                .orElseGet(() -> {
                    newImage.setId((int) id);
                    return repository.save(newImage);
                });
    }

    @GetMapping("{user_email}")
    Image one(@PathVariable String user_email) {

        return repository.findByUserEmail(user_email);
    }

    @PutMapping("{user_email}")
    Image replaceImage(@RequestBody Image newImage, @PathVariable String user_email) {

        Image image = repository.findByUserEmail(user_email);
        image.setPath(newImage.getPath());
        return repository.save(image);
    }

    @DeleteMapping("{id}")
    void deleteImage(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
