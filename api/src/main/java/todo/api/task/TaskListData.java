package todo.api.task;

import java.time.OffsetDateTime;

public record TaskListData(
        Long id,
        String title,
        String description,
        OffsetDateTime deadline,
        boolean completed,
        boolean isOverdue
) {
    public TaskListData(Task task) {
        this(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.isCompleted(),
                task.isOverdue()
        );
    }
}
