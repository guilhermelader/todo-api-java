package todo.api.task;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Table(name = "tasks")
@Entity(name = "Task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private OffsetDateTime deadline;
    @Setter
    private boolean completed;

    public Task(TaskData task) {
        this.title = task.title();
        this.description = task.description();
        this.deadline = task.deadline();
        this.completed = task.completed();  // Copiar o valor de completed
    }

    public void updateFrom(TaskData taskData) {
        this.title = taskData.title();
        this.description = taskData.description();
        this.deadline = taskData.deadline();
        this.completed = taskData.completed(); // Atualizar completed
    }

    public boolean isOverdue() {
        return deadline != null && deadline.isBefore(OffsetDateTime.now()) && !completed;
    }
}
