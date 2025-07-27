package com.tracker.tasks.mappers;

import com.tracker.tasks.domain.dto.TaskListDto;
import com.tracker.tasks.domain.entities.TaskList;

public interface TaskListMapper {

    TaskList fromDto(TaskListDto taskListDto); // converts a TaskListDto to a TaskList entity
    TaskListDto toDto(TaskList taskList); // converts a TaskList entity to a TaskListDto


}


// bu projede manuel mapper kullandım.
// automatic mapping icin yapı  @Mapper annotaion'ı kullanarak yapılıyor.
// mantığı anlarsam automatic gecerim.