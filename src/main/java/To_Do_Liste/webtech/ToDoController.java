package To_Do_Liste.webtech;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    private final ToDoRepository repo;

    public ToDoController(ToDoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<ToDo> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public ToDo create(@RequestBody ToDo todo) {
        todo.setDone(false);
        return repo.save(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDo> update(@PathVariable Long id, @RequestBody ToDo incoming) {
        return repo.findById(id)
                .map(todo -> {
                    todo.setText(incoming.getText());
                    todo.setDone(incoming.isDone());
                    return ResponseEntity.ok(repo.save(todo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
