package com.tracker.tasks.mappers;

import com.tracker.tasks.domain.dto.TaskReminderDto;
import com.tracker.tasks.domain.entities.TaskReminder;

public interface TaskReminderMapper {
    TaskReminder fromDto(TaskReminderDto dto);
    TaskReminderDto toDto(TaskReminder entity);
}
