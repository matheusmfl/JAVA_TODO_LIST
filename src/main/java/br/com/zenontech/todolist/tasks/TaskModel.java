package br.com.zenontech.todolist.tasks;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;
  private String description;
  @Column(length = 50)
  private String title;
  private LocalDateTime startedAt;
  private LocalDateTime endingAt;
  private String priority;
  private UUID idUser;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @CreationTimestamp
  private LocalDateTime updatedAt;

}
