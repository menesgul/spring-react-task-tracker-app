package com.tracker.tasks.repositories;

import com.tracker.tasks.domain.entities.TaskReminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskReminderRepository extends JpaRepository<TaskReminder, UUID> {
}
