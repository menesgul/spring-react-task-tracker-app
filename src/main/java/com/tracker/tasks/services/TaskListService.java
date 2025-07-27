package com.tracker.tasks.services;


import com.tracker.tasks.domain.entities.Task;
import com.tracker.tasks.domain.entities.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListService {
    List<TaskList> listTaskList();
    TaskList createTaskList(TaskList taskList);
    Optional<TaskList>getTaskList(UUID id);
   // o id ye ait task list yoksa Optional.empty() , thus optional.
    TaskList updateTaskList(UUID taskListId, TaskList taskList);
    void deleteTaskList(UUID taskListId);

}






// --- interface metotları ----


/*
- createTaskList() methodu service layer içindedir.
- Controller, bu metodu çağırarak iş mantığını bu katmana bırakır.
- Service, repository üzerinden veriyi veritabanına yollar.

 */