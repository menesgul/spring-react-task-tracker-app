package com.tracker.tasks.services.impl;

import com.tracker.tasks.domain.entities.TaskList;
import com.tracker.tasks.repositories.TaskListRepository;
import com.tracker.tasks.services.TaskListService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<TaskList> listTaskList() {
        return taskListRepository.findAll();
    }

    @Override
    public TaskList createTaskList(TaskList taskList) {  //  TaskListService interface’inde tanımlı olan createTaskList metodunu implement ettim.
        if(null != taskList.getId()) {
            throw new IllegalArgumentException("Task list already has an ID !");
        }
        if(null == taskList.getTitle() || taskList.getTitle().isBlank()){
            throw new IllegalArgumentException("Task list title cannot be empty !");
        }

        LocalDateTime now = LocalDateTime.now();
        return taskListRepository.save(new TaskList(  //  Burada yeni bir TaskList nesnesi oluşturuyorsun
            null, // id db tarafından assign edilcek
            taskList.getTitle(),
            taskList.getDescription(),
            null,
            now,
            now
            ));
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
    public Optional<TaskList> getTaskList(UUID id) {

        return taskListRepository.findById(id);
    }

    @Override
    public TaskList updateTaskList(UUID taskListId, TaskList taskList) {
        if(null == taskList.getId()) { // check id is empty or not
            throw new IllegalArgumentException("Task list does not have an ID !");
        }

        if(!Objects.equals(taskList.getId(), taskListId)) { // is id matched or not
            throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted  !");
        }
      TaskList existingTaskList =  taskListRepository.findById(taskListId).orElseThrow(() -> // objenin diğer var larini update ve db ye saveleme
                new IllegalArgumentException("Task list not found ! "));

            existingTaskList.setTitle(taskList.getTitle());
            existingTaskList.setDescription(taskList.getDescription());
            existingTaskList.setUpdated(LocalDateTime.now());
          return   taskListRepository.save(existingTaskList);

    }

    @Override
    public void deleteTaskList(UUID taskListId) {
        taskListRepository.deleteById(taskListId);
// we don't check if the task list exists before deleting it
//spring data jpa delete by ID method handles non-existing entities gracefully
    }
}
