package drack_company.demo.dto;

import drack_company.demo.entity.tasktracker;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record TaskRequest(
        @NotBlank(message = "Name can`t be void  ")
        String title,

        String description,
        String subjectName,

@FutureOrPresent(message = "Dedline must be in future ")
        LocalDateTime dueDate,

        tasktracker status
                          ) {
}
