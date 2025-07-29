package com.tracker.tasks.domain.dto;

import com.tracker.tasks.domain.entities.ReminderRepeatType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record TaskReminderDto(
        UUID id,
        boolean isDateEnabled,
        boolean isTimeEnabled,
        LocalDate date,
        LocalTime time,
        ReminderRepeatType repeatType,
        boolean enabled
)
{

}
