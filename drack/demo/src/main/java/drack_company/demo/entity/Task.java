package drack_company.demo.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor

public class Task {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  @Column
    private String title;

 @Column(columnDefinition = "TEXT")
    private String description;

 @Column(name = "subject_names")
    private String subject_names;

 @Column(name = "due_date")
    private LocalDateTime duedate;

 @Enumerated(EnumType.STRING)
 @Column(nullable = false)
 private tasktracker status = tasktracker.TODO;

 @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
 @PrePersist
    protected void onCreated(){
this.createdAt = LocalDateTime.now();
 }

}

