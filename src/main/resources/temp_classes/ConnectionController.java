package temp_classes;

import org.example.ted.exceptions.ConnectionNotFoundException;
import org.example.ted.models.Connection;
import org.example.ted.models.User;
import org.example.ted.repositories.ConnectionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/connections")
public class ConnectionController {

    private final ConnectionRepository repository;

    ConnectionController(ConnectionRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping
    List<Connection> all() {
        return repository.findAll();
    }

    @GetMapping("/user/{user}")
    List<Connection> allByUser(@PathVariable(value = "user") User user) {
        return repository.findByUser(user);
    }

    @PostMapping
    Connection newConnection(@RequestBody Connection newConnection) {
        return repository.save(newConnection);
    }

    // Single item

    @GetMapping("{id}")
    Connection one(@PathVariable long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ConnectionNotFoundException(id));
    }

//    @PutMapping("{id}")
//    Comment replaceComment(@RequestBody Comment newProfile, @PathVariable long id) {
//
//        return repository.findById(id)
//                .map(profile -> {
//                    profile.setCreatedBy(newProfile.getCreatedBy());
//                    profile.setCv(newProfile.getCv());
//                    profile.setExperience(newProfile.getExperience());
//                    profile.setEducation(newProfile.getEducation());
//                    profile.setSkills(newProfile.getSkills());
//                    return repository.save(profile);
//                })
//                .orElseGet(() -> {
//                    newProfile.setId((int) id);
//                    return repository.save(newProfile);
//                });
//    }

    @DeleteMapping("{id}")
    void deleteConnection(@PathVariable long id) {
        repository.deleteById(id);
    }
}
