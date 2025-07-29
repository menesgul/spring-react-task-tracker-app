package com.tracker.tasks.mappers.impl;

import com.tracker.tasks.domain.dto.TaskDto;
import com.tracker.tasks.domain.dto.TaskReminderDto;
import com.tracker.tasks.domain.entities.Task;
import com.tracker.tasks.mappers.TaskMapper;
import com.tracker.tasks.mappers.TaskReminderMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {

    private final TaskReminderMapper taskReminderMapper;

    public TaskMapperImpl(TaskReminderMapper taskReminderMapper) {
        this.taskReminderMapper = taskReminderMapper;
    }


    @Override
    public Task fromDto(TaskDto taskDto) {
       return new Task(
        taskDto.id(),
        taskDto.title(),
        taskDto.description(),
        taskDto.dueDate(),
        taskDto.status(),
        taskDto.priority(),
        null,
        null,
        null,
       taskReminderMapper.fromDto(taskDto.reminder())
       );
    }
    /*
    Kullanıcıdan gelen TaskDto objesini alıyor (örneğin bir formdan gelen veri).
    Yeni bir Task (Entity) nesnesi oluşturuyor.
    createdAt, updatedAt, taskList alanlarını şimdilik null bırakıyorsun (bunları genelde service katmanı doldurur).
*/
    @Override
    public TaskDto toDto(Task task) {
        TaskReminderDto reminderDto = taskReminderMapper.toDto(task.getReminder()); // ✅ reminder Entity'den DTO'ya

        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescripiton(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus(),
                reminderDto
        );
        /*
- Veritabanından gelen Task objesini alıyor.
- UI’ye göstermek için uygun hale getiriyor (TaskDto).
- Sensitive (gizli) bilgiler yok, sade bir yapı.

         */
    }
}
