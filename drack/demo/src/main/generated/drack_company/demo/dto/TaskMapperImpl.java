package drack_company.demo.dto;

import drack_company.demo.entity.Task;
import drack_company.demo.entity.tasktracker;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T16:01:02+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Microsoft)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toEntity(TaskRequest request) {
        if ( request == null ) {
            return null;
        }

        Task task = new Task();

        task.setTitle( request.title() );
        task.setDescription( request.description() );
        task.setSubjectName( request.subjectName() );
        task.setStatus( request.status() );

        return task;
    }

    @Override
    public TaskResponse toResponse(Task task) {
        if ( task == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String description = null;
        String subjectName = null;
        tasktracker status = null;
        LocalDateTime createdAt = null;
        String attachedFileName = null;

        id = task.getId();
        title = task.getTitle();
        description = task.getDescription();
        subjectName = task.getSubjectName();
        status = task.getStatus();
        createdAt = task.getCreatedAt();
        attachedFileName = task.getAttachedFileName();

        LocalDateTime dueDate = null;

        TaskResponse taskResponse = new TaskResponse( id, title, description, subjectName, dueDate, status, createdAt, attachedFileName );

        return taskResponse;
    }

    @Override
    public List<TaskResponse> toResponseList(List<Task> tasks) {
        if ( tasks == null ) {
            return null;
        }

        List<TaskResponse> list = new ArrayList<TaskResponse>( tasks.size() );
        for ( Task task : tasks ) {
            list.add( toResponse( task ) );
        }

        return list;
    }
}
