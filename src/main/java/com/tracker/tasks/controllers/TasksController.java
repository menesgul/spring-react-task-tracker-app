package com.tracker.tasks.controllers;


import com.tracker.tasks.domain.dto.TaskDto;
import com.tracker.tasks.domain.entities.Task;
import com.tracker.tasks.mappers.TaskMapper;
import com.tracker.tasks.repositories.TaskRepository;
import com.tracker.tasks.services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/task-lists/{task_list_id}/tasks")

public class TasksController {

    private final TaskService taskService;
    private final TaskMapper taskMapper; // dönüsüm icin daima mapper koyuyorum.
    private final TaskRepository taskRepository;

    public TasksController(TaskService taskService, TaskMapper taskMapper, TaskRepository taskRepository) { // injection
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<TaskDto> listTasks(@PathVariable("task_list_id") UUID taskListId) {
        return taskService.listTasks(taskListId)
                .stream()
                .map(taskMapper::toDto)
                .toList();

    }

    @PostMapping
    public TaskDto createTask(@PathVariable("task_list_id") UUID taskListId,
                              @RequestBody TaskDto taskDto) {
        //  Gelen DTO'yu (kullanıcıdan gelen veriyi) Entity'ye çeviriyoruz
        Task createdTask = taskService.createTask(taskListId, // bu id kullanıcıdan gelen Id
                taskMapper.fromDto(taskDto)); // DTO → Entity dönüşümü
        return taskMapper.toDto(createdTask); //  Kaydedilen Entity'i tekrar DTO'ya çevirip döndürüyoruz
    }


    @GetMapping(path = "/{task_id}")
    public Optional<TaskDto> getTask(@PathVariable("task_list_id") UUID taskListId, // urlden parametre alma işlemi
                                     @PathVariable("task_id") UUID taskId) {

        return taskService.getTask(taskListId, taskId).map(taskMapper::toDto); // servis katmanına istek atılır, ilgili taski
        // db'den bulmasını ister. bulunca entity -> Dto yap return et.
    }

    @PutMapping(path = "/{task_id}")
    public TaskDto updateTask(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("task_id") UUID taskId,
            @RequestBody TaskDto taskDto
    ) {
        Task updatedTask = taskService.updateTask(taskListId, taskId,
                taskMapper.fromDto(taskDto));
        return taskMapper.toDto(updatedTask);

    }

    @DeleteMapping("/{task_id}")
    public void deleteTask(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("task_id") UUID taskId
            ) {
        taskService.deleteTask(taskListId, taskId);
    }
}
