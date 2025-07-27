package com.tracker.tasks.controllers;


import com.tracker.tasks.domain.dto.TaskListDto;
import com.tracker.tasks.domain.entities.TaskList;
import com.tracker.tasks.mappers.TaskListMapper;
import com.tracker.tasks.services.TaskListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path="api/task-lists")
public class TaskListController {

    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;

    public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper) {
        this.taskListService = taskListService;
        this.taskListMapper = taskListMapper;
    }


    @GetMapping
    public List<TaskListDto> listTaskLists() {
      return  taskListService.listTaskList().stream()
                .map(taskListMapper::toDto) // dbdeki data DTO formatına dönüsüp listeleniyor.
                .toList();
    }

    @PostMapping
    public TaskListDto createTaskList(@RequestBody TaskListDto taskListDto) { // burda api'den   JSON formatındaki body, otomatik olarak taskListDto nesnesine dönüştürülür.
   TaskList createdTaskList =  taskListService.createTaskList( // burada servis katmanındaki metotu cağırdım.
                        // bu metot calıstı iste title boş mu baktı id var mı baktı her sey uygunsa repository.save() yapıp db'ye kaydetti

             taskListMapper.fromDto(taskListDto) //  DTO → Entity (TaskList) dönüşümü yapılır.
     );                                          // Çünkü Service katmanı TaskList (entity) ile çalışır, DTO ile değil.


        return taskListMapper.toDto(createdTaskList); // db ye kaydedilen objeyi  direkt fe ye göndermek istemiyoruz.
                                                      // gizlemek sadelestirmek istediğimiz veriler vardı cünkü , id created date gibi.
                                                      // bu yüzden Entity -> DTO dönüsümü yapıp return olur.

    }
        @GetMapping(path= "/{task_list_id}")
        public Optional<TaskListDto> getTaskList(@PathVariable("task_list_id") UUID taskListId) {
            return  taskListService.getTaskList(taskListId).map(taskListMapper::toDto);

        }
/*       burada @PathVariable url yolundaki {task_list_id} kısmını alır.
         UUID taskListId: → Bu ID'yi UUID tipine çevirir

 taskListService.getTaskList(taskListId)
→ Servis layerına gidilir.
→ Bu id'ye sahip TaskList var mı diye Repository'e sorulur.
→ Dönüş: Optional<TaskList>

.map(taskListMapper::toDto)
→ Optional içinde değer varsa (TaskList), taskListMapper.toDto() metodunu çağırır. mapper convertleme yapıyordu..
→ Bu metot, TaskList nesnesini TaskListDto'ya çevirir. sonuc fe de döner.

*/
    @PutMapping(path = "/{task_list_id}")
    public TaskListDto updateTaskList(
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody TaskListDto taskListDto) {

      TaskList updatedTaskList =   taskListService.updateTaskList(
                taskListId,
                taskListMapper.fromDto(taskListDto)  // dto to real object form. bu sayede servis katmanında işlem yapıp gercek nesne haliyle db de update edilir
        );
        return taskListMapper.toDto(updatedTaskList); // fe 'ye objectin Dto formu returnlenir.
    }

    @DeleteMapping(path = "/{task_list_id}")
    public void deleteTaskList(@PathVariable("task_list_id") UUID taskListId) {
        taskListService.deleteTaskList(taskListId);
    }

}
