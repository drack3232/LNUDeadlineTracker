package drack_company.demo.dto;

import drack_company.demo.entity.tasktracker;

import java.time.LocalDateTime;

public record TaskResponse (Long id,
                            String title,
                            String description,
                            String subjectName,
                            LocalDateTime dueDate,
                            tasktracker status,
                            LocalDateTime createdAt,
                            String attachedFileName
                            ){
}
