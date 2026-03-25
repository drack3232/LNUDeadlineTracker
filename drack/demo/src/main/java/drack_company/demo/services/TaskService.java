package drack_company.demo.services;
import drack_company.demo.entity.tasktracker;
import drack_company.demo.entity.Task;
import drack_company.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    TaskService(TaskRepository taskRepository){this.taskRepository = taskRepository;}

    public List<Task> getAllTask (){return taskRepository.findAll();}
    public List<Task> getTaskByStatus(tasktracker status){return taskRepository.findByStatus(status);}
    public  Task createTask(Task task){
        if(taskRepository.existsByTitle(task.getTitle())) {
        throw new IllegalArgumentException("Task with title: " + task.getTitle() + " have created");
        }
        return taskRepository.save(task);
    }

    public Task  updateTaskStatus(Long id, tasktracker newStatus){
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Task with id: " + id + " not found"));
        existingTask.setStatus(newStatus);
        return taskRepository.save(existingTask);
    }
    public void deleteTask(Long id){
        if(!taskRepository.existsById(id)){
            throw new RuntimeException("Impossible delete:task with Id " +  id  + " not found");

        }
        taskRepository.deleteById(id);
    }
}
