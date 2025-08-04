package com.tracker.tasks.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tracker.tasks.domain.entities.TaskList;
import com.tracker.tasks.domain.entities.User;
import com.tracker.tasks.repositories.TaskListRepository;
import com.tracker.tasks.services.TaskListService;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<TaskList> listTaskList(User user) {
        return taskListRepository.findAllByOwner(user);
    }

    @Override
    public TaskList createTaskList(TaskList taskList, User user) {
        if(null != taskList.getId()) {
            throw new IllegalArgumentException("Task list already has an ID !");
        }
        if(null == taskList.getTitle() || taskList.getTitle().isBlank()){
            throw new IllegalArgumentException("Task list title cannot be empty !");
        }
        LocalDateTime now = LocalDateTime.now();
        TaskList newTaskList = new TaskList(
            null, // id db tarafından assign edilcek
            taskList.getTitle(),
            taskList.getDescription(),
            null,
            now,
            now,
            user
        );
        return taskListRepository.save(newTaskList);
    }

// Girdi olarak bir TaskList nesnesi alır,
// çıktı olarak da veritabanına kayıt edilmiş TaskList döner.

/*
🔽 İstek
         [Frontend - axios.fetch()]
        ↓
        [Controller (REST)]
        ↓
        [Service Layer (İş mantığı)]
        ↓
        [Repository (Veri erişimi / DB)]
        ↓
        [Veritabanı]

        🔼 Yukarıya doğru veriler döner
*/
    @Override // TaskListService interface'ini implement ettik.
    public Optional<TaskList> getTaskList(UUID id, User user) {

        return taskListRepository.findByIdAndOwner(id, user);
    }

    @Override
    public TaskList updateTaskList(UUID taskListId, TaskList taskList, User user) {
        if(null == taskList.getId()) { // check id is empty or not
            throw new IllegalArgumentException("Task list does not have an ID !");
        }

        if(!Objects.equals(taskList.getId(), taskListId)) { // is id matched or not
            throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted  !");
        }
      TaskList existingTaskList =  taskListRepository.findByIdAndOwner(taskListId, user)
                .orElseThrow(() -> // objenin diğer var larini update ve db ye saveleme
                new IllegalArgumentException("Task list not found or not owned by user! "));

            existingTaskList.setTitle(taskList.getTitle());
            existingTaskList.setDescription(taskList.getDescription());
            existingTaskList.setUpdated(LocalDateTime.now());
          return   taskListRepository.save(existingTaskList);

    }

    @Override
    public void deleteTaskList(UUID taskListId, User user) {
        TaskList existingTaskList = taskListRepository.findByIdAndOwner(taskListId, user)
                .orElseThrow(() -> new IllegalArgumentException("Task list not found or not owned by user! "));
        taskListRepository.deleteById(existingTaskList.getId());
// we don't check if the task list exists before deleting it
//spring data jpa delete by ID method handles non-existing entities gracefully
    }
}
