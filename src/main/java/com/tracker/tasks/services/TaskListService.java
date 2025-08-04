package com.tracker.tasks.services;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tracker.tasks.domain.entities.TaskList;
import com.tracker.tasks.domain.entities.User;

public interface TaskListService {
    List<TaskList> listTaskList(User user);
    TaskList createTaskList(TaskList taskList, User user);
    Optional<TaskList>getTaskList(UUID id, User user);
   // o id ye ait task list yoksa Optional.empty() , thus optional.
    TaskList updateTaskList(UUID taskListId, TaskList taskList, User user);
    void deleteTaskList(UUID taskListId, User user);

}






// --- interface metotları ----


/*
- createTaskList() methodu service layer içindedir.
- Controller, bu metodu çağırarak iş mantığını bu katmana bırakır.
- Service, repository üzerinden veriyi veritabanına yollar.

 */