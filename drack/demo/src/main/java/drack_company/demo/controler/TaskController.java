package drack_company.demo.controler;
import drack_company.demo.entity.*;
import drack_company.demo.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService ;
    public TaskController(TaskService taskService){this.taskService = taskService;}

    @GetMapping
    public List<Task> getTask(@RequestParam(required = false ) tasktracker status){
        if(status != null){
            return taskService.getTaskByStatus(status);
        }
        return taskService.getAllTask();
    }
@PostMapping
    public Task craeteTask (@Valid @RequestBody Task task){
   return taskService.createTask(task);
}
@PatchMapping("/{id}/status")
    public Task updateTask(@PathVariable Long id, @RequestParam tasktracker status){
        return taskService.updateTaskStatus(id, status);
}
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
