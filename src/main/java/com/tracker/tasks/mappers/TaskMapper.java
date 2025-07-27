package com.tracker.tasks.mappers;

import com.tracker.tasks.domain.dto.TaskDto;
import com.tracker.tasks.domain.entities.Task;

public interface TaskMapper {

    Task fromDto(TaskDto taskDto); // DTO → Entity

    TaskDto toDto(Task task); // Entity → DTO

// Mapper → sadece veri dönüşümünü yapar
// Controller ⇄ DTO ⇄ [Mapper] ⇄ Entity ⇄ Repository ⇄ DB
//Bana TaskDto ver, ben sana Task dönerim.
//Bana Task ver, ben sana TaskDto dönerim.
}
