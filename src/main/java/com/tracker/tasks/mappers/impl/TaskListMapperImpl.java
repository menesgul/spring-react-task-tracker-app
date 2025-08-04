package com.tracker.tasks.mappers.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.tracker.tasks.domain.dto.TaskListDto;
import com.tracker.tasks.domain.entities.Task;
import com.tracker.tasks.domain.entities.TaskList;
import com.tracker.tasks.domain.entities.TaskStatus;
import com.tracker.tasks.mappers.TaskListMapper;
import com.tracker.tasks.mappers.TaskMapper;

@Component
public class TaskListMapperImpl implements TaskListMapper {

   private final TaskMapper taskMapper;

    //  TaskListMapperImpl sınıfının içinde TaskMapper'ı kullanabilmek için.

    public TaskListMapperImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
// TaskListMapperImpl nesnesini oluşturduk , TaskMapper gelecek
// onu icine koycaz. TaskMapperImpl @Component li . Spring bellek
// yönetiminde Spring uygulama başlarken TaskMapperImpl'i oluşturuyor.
// constructor'da TaskMapper istenildiğini görünce, hazır olanı  veriyor.
//  kısaca DI anlattım.
// or  @RequiredArgsConstructor

    }

    @Override
    public TaskList fromDto(TaskListDto taskListDto) {
        return new TaskList(
                taskListDto.id(),
                taskListDto.title(),
                taskListDto.description(),
                Optional.ofNullable(taskListDto.tasks()) //  // Eğer tasks null değilse
                        .map(tasks ->
                                tasks.stream() // akış oluştur
                                        .map(taskMapper::fromDto) //  // Her TaskDto'yu Task'a çevir
                                .toList() // Stream'den sonra elde ettiğin sonucu tekrar bir List olarak almanı sağlar.

                        ).orElse(null),
                null,
                null,
                null
        );
//TaskListDto → TaskList dönüşümünü yapar.
//Yani frontend'den gelen verileri, backend'in anlayacağı Entity sınıfına çevirir.
// taskListDto.tasks()   -- Bu TaskListDto içindeki görevlerin (TaskDto) listesidir.
// .map(taskMapper::fromDto) -- Her TaskDto nesnesini, taskMapper.fromDto(taskDto) ile bir Task nesnesine çevir
// map(...)	Her elemanı dönüştürür.
//  ).orElse(null), task null is direkt null döndür.

    }

    @Override
    public TaskListDto toDto(TaskList taskList) { // 	Görev listesini sayısal olarak DTO’ya çevirir (Entity → Slim DTO)
        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                Optional.ofNullable(taskList.getTasks())// // Eğer liste null değilse
                        .map(List::size)  // listedeki eleman sayısını al
                        .orElse(0),    //  liste null ise sıfır döndür
                calculateTaskListProgress(taskList.getTasks()),
                Optional.ofNullable(taskList.getTasks())
                        .map(tasks ->
                        tasks.stream().map(taskMapper::toDto).toList()
                                ).orElse(null)

        );
    }
    private Double calculateTaskListProgress(List<Task> tasks) {
        if (null == tasks) {
            return null;
        }
        long closedTaskCount = tasks.stream().filter(task ->
                        TaskStatus.CLOSED == task.getStatus()
                ).count();

        return (double) closedTaskCount / tasks.size();
    }
}
