package no.hvl.dat250.rest.todos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/todos")
public class TodoController {

  public static final String TODO_WITH_THE_ID_X_NOT_FOUND = "Todo with the id %s not found!";
  private final List<Todo> todos = new ArrayList<>();
  private final AtomicLong counter = new AtomicLong();


  @PostMapping
  public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
    todo.setId(counter.incrementAndGet());
    todos.add(todo);
    return new ResponseEntity<>(todo, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Todo>> getAllTodos() {
    return new ResponseEntity<>(todos, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getTodoById(@PathVariable Long id) {
    Optional<Todo> todo = todos.stream().filter(t -> t.getId().equals(id)).findFirst();
    if (todo.isPresent()) {
      return new ResponseEntity<>(todo.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, id), HttpStatus.NOT_FOUND);
    }
  }



  @PutMapping("/{id}")
  public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
    Optional<Todo> existingTodo = todos.stream().filter(t -> t.getId().equals(id)).findFirst();
    if (existingTodo.isPresent()) {
      Todo todo = existingTodo.get();
      todo.setSummary(updatedTodo.getSummary());
      todo.setDescription(updatedTodo.getDescription());
      return new ResponseEntity<>(todo, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
    Optional<Todo> todo = todos.stream().filter(t -> t.getId().equals(id)).findFirst();
    if (todo.isPresent()) {
      todos.remove(todo.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, id), HttpStatus.NOT_FOUND);
    }
  }
}
