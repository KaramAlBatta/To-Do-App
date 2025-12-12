package To_Do_Liste.webtech;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(
        origins = "https://to-do-app-1-l2tv.onrender.com",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS }
)
public class ToDoController {

    private final ToDoRepository repo;

    public ToDoController(ToDoRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/api/todos")
    public List<ToDo> getAllToDos() {
        return repo.findAll();
    }

    @PostMapping("/api/todos")
    public ToDo createToDo(@RequestBody ToDo todo) {
        return repo.save(todo);
    }
}
