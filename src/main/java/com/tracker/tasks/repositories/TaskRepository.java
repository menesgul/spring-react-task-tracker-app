package com.tracker.tasks.repositories;


import com.tracker.tasks.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByTaskListId(UUID taskListId);
    Optional<Task> findByTaskListIdAndId(UUID taskListId, UUID id);
    void deleteByTaskListIdAndId(UUID taskListId, UUID id);
}


/*
Bu bir interface’tir ve JpaRepository'yi genişletir.
 Spring Data JPA, bu interface’i çalıştırılabilir hale getirir,
  yani arkada kod üretir. Senin kendin SQL yazmana gerek kalmaz.
 ex :
      List<Task> findByTaskListId(UUID taskListId);
  Spring şunu anlar:
  - Task içinde TaskList adlı bir field var.
  - TaskList içinde de id var.
  - Bu metoda taskListId parametresi gönderilmiş.
  - O zaman: task.taskList.id = :taskListId olan tüm Task’ları getir!

 */