package drack_company.demo.controler;
import drack_company.demo.dto.TaskMapper;
import drack_company.demo.dto.TaskRequest;
import drack_company.demo.dto.TaskResponse;
import drack_company.demo.entity.*;
import drack_company.demo.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService ;
    private final TaskMapper taskMapper;
    public TaskController(TaskService taskService, TaskMapper taskMapper){
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public List<TaskResponse> getTask(@RequestParam(required = false) tasktracker status){
        List<Task> tasks;
        if(status != null){
            tasks = taskService.getTaskByStatus(status);
        } else {
            tasks = taskService.getAllTask();
        }
        return taskMapper.toResponseList(tasks);
    }
@PostMapping
    public TaskResponse craeteTask (@Valid @RequestBody TaskRequest request){
  Task taslToSave = taskMapper.toEntity(request);
  Task savedTask = taskService.createTask(taslToSave);
  return taskMapper.toResponse(savedTask);
}
@PatchMapping("/{id}/status")
    public TaskResponse updateTask(@PathVariable Long id, @RequestParam tasktracker status){
       Task updateTask = taskService.updateTaskStatus(id, status);
        return taskMapper.toResponse(updateTask);
}
@PostMapping(value = "/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public TaskResponse uploadTaskFile(@PathVariable Long id, @RequestParam("file")MultipartFile file){
        Task updateTask = taskService.attachFileToTask(id, file);

        return taskMapper.toResponse(updateTask);
}
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
