package drack_company.demo.dto;
import drack_company.demo.entity.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
   Task toEntity(TaskRequest request);
   TaskResponse toResponse(Task task);
   List<TaskResponse> toResponseList(List<Task> tasks);
}
