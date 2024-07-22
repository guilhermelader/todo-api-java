package todo.api.task;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public record TaskData(
        @NotBlank String title,
        @NotBlank String description,
        @FutureOrPresent OffsetDateTime deadline,
        boolean completed
) {}
