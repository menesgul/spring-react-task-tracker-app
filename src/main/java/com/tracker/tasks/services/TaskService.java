package com.tracker.tasks.services;

import com.tracker.tasks.domain.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    List<Task> listTasks(UUID taskListId );
    Task createTask(UUID taskListId, Task task); // SPESİFİK taskliste assignlicam
    Optional<Task> getTask(UUID taskListId, UUID taskId); // x tasklistindeki y taskını bana getir , yoksa null döndür.
    Task updateTask(UUID taskListId, UUID taskId, Task task);
    void deleteTask(UUID taskListId, UUID taskId);
}
