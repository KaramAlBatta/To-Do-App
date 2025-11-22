package To_Do_Liste.webtech;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // Vue-Frontend erlauben
public class ToDoController {

    @GetMapping("/api/todos")
    public List<ToDo> getAllToDos() {
        return List.of(
                new ToDo(1L, "Vue.js-App fertigstellen", false),
                new ToDo(2L, "Code auf GitHub pushen", true),
                new ToDo(3L, "Backend testen", false)
        );
    }
}
