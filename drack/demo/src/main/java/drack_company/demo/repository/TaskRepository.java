package drack_company.demo.repository;
import drack_company.demo.entity.tasktracker;
import drack_company.demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(tasktracker status);

    List<Task> findAllByOrderByDuedateAsc();

    List<Task> findBySubjectNameAndStatus(String subjectName, tasktracker status);

    boolean existsByTitle(String title);
}