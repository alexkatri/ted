package temp_classes;

import org.example.ted.exceptions.ArticleMultimediaNotFoundException;
import org.example.ted.repositories.ArticleMultimediaRepository;
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
@RequestMapping("/multimedia")
public class ArticleMultimediaController {

    private final ArticleMultimediaRepository repository;

    private static String UPLOADED_FOLDER = "C://Users//sgt_o//IdeaProjects//ted//src//main//resources//templates";

    ArticleMultimediaController(ArticleMultimediaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    // Aggregate root

    @GetMapping
    List<ArticleMultimedia> all() {
        return repository.findAll().stream().sorted().toList();
    }

    @PostMapping
    public String singleFileUpload(@RequestParam("multimedia") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/multimedia/uploadStatus";
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
            ArticleMultimedia multimedia = new ArticleMultimedia(path.toString());
            repository.save(multimedia);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/multimedia/uploadStatus";
    }

    // Single item

    @GetMapping("{id}")
    ArticleMultimedia one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ArticleMultimediaNotFoundException(id));
    }

//    @PutMapping("{id}")
//    Article replaceArticle(@RequestBody Article newArticle, @PathVariable long id) {
//
//        return repository.findById(id)
//                .map(article -> {
//                    article.setCreatedBy(newArticle.getCreatedBy());
//                    article.setDate(newArticle.getDate());
//                    article.setViews(newArticle.getViews());
//                    article.setMultimediaPath(newArticle.getMultimediaPath());
//                    return repository.save(article);
//                })
//                .orElseGet(() -> {
//                    newArticle.setId((int) id);
//                    return repository.save(newArticle);
//                });
//    }

    @DeleteMapping("{id}")
    void deleteArticleMultimedia(@PathVariable long id) {
        repository.deleteById(id);
    }
}
