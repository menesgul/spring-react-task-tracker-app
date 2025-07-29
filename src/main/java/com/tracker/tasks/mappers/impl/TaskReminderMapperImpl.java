package com.tracker.tasks.mappers.impl;

import com.tracker.tasks.domain.dto.TaskReminderDto;
import com.tracker.tasks.domain.entities.TaskReminder;
import com.tracker.tasks.mappers.TaskReminderMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskReminderMapperImpl implements TaskReminderMapper {

    @Override
    public TaskReminder fromDto(TaskReminderDto dto) {
        if (dto == null) return null;

        System.out.println("Gelen DTO (Reminder):");
        System.out.println("Date: " + dto.date());
        System.out.println("Time: " + dto.time());
        System.out.println("Repeat: " + dto.repeatType());

        TaskReminder reminder = new TaskReminder();
        reminder.setId(dto.id());
        reminder.setDateEnabled(dto.isDateEnabled());
        reminder.setTimeEnabled(dto.isTimeEnabled());
        reminder.setDate(dto.date());
        reminder.setTime(dto.time());
        reminder.setRepeatType(dto.repeatType());
        reminder.setEnabled(dto.enabled());
        return reminder;
    }

    @Override
    public TaskReminderDto toDto(TaskReminder reminder) {
        if (reminder == null) return null;

        return new TaskReminderDto(
                reminder.getId(),
                reminder.isDateEnabled(),
                reminder.isTimeEnabled(),
                reminder.getDate(),
                reminder.getTime(),
                reminder.getRepeatType(),
                reminder.isEnabled()
        );
    }

}
