package todo.api.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import todo.api.task.Task;
import todo.api.task.TaskData;
import todo.api.task.TaskListData;
import todo.api.task.TaskRepository;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {
    @Autowired
    private TaskRepository repository;

    @GetMapping
    public ResponseEntity<Page<TaskListData>> list(Pageable pagination) {
        Page<TaskListData> tasks = repository.findAll(pagination)
                .map(TaskListData::new);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return repository.findById(Long.valueOf(id))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, String>> create(@RequestBody @Valid TaskData taskData) {
        Task task = new Task(taskData);
        task.setDeadline(task.getDeadline().withOffsetSameInstant(OffsetDateTime.now().getOffset()));
        Task savedTask = repository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Task created successfully", "task", savedTask.toString()));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @RequestBody @Valid TaskData taskData) {
        Optional<Task> existingTaskOptional = repository.findById(id);
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            existingTask.updateFrom(taskData);
            existingTask.setDeadline(taskData.deadline().withOffsetSameInstant(OffsetDateTime.now().getOffset())); // Garantir UTC
            repository.save(existingTask);
            return ResponseEntity.ok(Map.of("message", "Task updated successfully", "task", existingTask.toString()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Task not found"));
        }
    }

    @PatchMapping("/{id}/completed")
    @Transactional
    public ResponseEntity<Map<String, String>> updateCompleted(@PathVariable Long id, @RequestParam boolean completed) {
        Optional<Task> existingTaskOptional = repository.findById(id);
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            existingTask.setCompleted(completed);
            repository.save(existingTask);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Task completion status updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Task not found"));
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        Optional<Task> existingTaskOptional = repository.findById(id);
        if (existingTaskOptional.isPresent()) {
            repository.delete(existingTaskOptional.get());
            return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Task not found"));
        }
    }
}
