package com.tracker.tasks.services.impl;

import com.tracker.tasks.domain.entities.*;
import com.tracker.tasks.repositories.TaskListRepository;
import com.tracker.tasks.repositories.TaskReminderRepository;
import com.tracker.tasks.repositories.TaskRepository;
import com.tracker.tasks.services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final TaskReminderRepository taskReminderRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskListRepository taskListRepository,
                           TaskReminderRepository taskReminderRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
        this.taskReminderRepository = taskReminderRepository;
    }

    @Override
    public List<Task> listTasks(UUID taskListId) {
        return taskRepository.findByTaskListId(taskListId);
    }

    @Transactional  // there are two call to db with repo methods . bu yüzden ekledik.
    @Override  // valide et , cekilmeyen değerleri  assign et , task objeni creat et - icini doldur return and save
    public Task createTask(UUID taskListId, Task task) {
        if (null != task.getId()) { // eğer id gelirse bu kayıtlıdır hata fırlatalım
            throw new IllegalArgumentException("Task already has an ID");
        }       // title bossa hata fırlatsın
        if (null == task.getTitle() || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        TaskPriority taskPriority = Optional.ofNullable(task.getPriority())
                .orElse(TaskPriority.MEDIUM); // pri. bos bırakırsa öyle gelirse default medium yapsın dedim

        TaskStatus taskStatus = TaskStatus.OPEN; // enum sabit ataması
        // verilen tasklist id ile db den tasklist nesnesi cekilir , yoksa hata fırlat.
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new IllegalArgumentException("Task does not exist"));

        LocalDateTime now = LocalDateTime.now(); // create update zamanı icin

        Task taskToSave = new Task( // yeni task objesi yaratıldı
                null,
                task.getTitle(),
                task.getDescripiton(),
                task.getDueDate(),
                taskStatus,
                taskPriority,
                taskList,
                now,
                now,
                task.getReminder()
        );
        return taskRepository.save(taskToSave);

    }
    /*
     task.getTitle()	 isEmpty() sonucu	isBlank() sonucu
      "Yemek yap"       	false           	false
          ""	            true	            true
          " "               false	            true
         null	  ❌ Hata (NullPointerException)❌ Hata (NullPointerException)

kullanıcı bosluk bırakırsa  isEmpty boş değil diyecek
 */


    @Override
    public Optional<Task> getTask(UUID taskListId, UUID taskId) {
        if (null == taskListId || null == taskId) {
            throw new IllegalArgumentException("Task ID or TaskList ID cannot be empty");
        }

        return taskRepository.findByTaskListIdAndId(taskListId, taskId);
    }

    @Transactional
    @Override
    public Task updateTask(UUID taskListId, UUID taskId, Task task) {
        if (null == taskListId || null == taskId) {
            throw new IllegalArgumentException("Task ID or TaskList ID cannot be empty");
        }
        if (!Objects.equals(taskId, task.getId())) {
            throw new IllegalArgumentException("Task IDs do not match");
        }
        if (null == task.getPriority()) {
            throw new IllegalArgumentException("Task priority cannot be empty");
        }
        if (null == task.getStatus()) {
            throw new IllegalArgumentException("Task status cannot be empty");
        }
        // bir dahakine (record) dto classında @Valid, @NotNull, @NotBlank,
        // @Size, vs. gibi annotation’ları kullan.
        Task existingTask = taskRepository.findByTaskListIdAndId(taskListId, taskId) // eritabanından gelen mevcut (persisted) bir Task objesini belleğe almak
                .orElseThrow(() -> new IllegalArgumentException("Task does not exist"));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescripiton(task.getDescripiton());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setUpdated(LocalDateTime.now());


        if (task.getReminder() != null) {
            TaskReminder incomingReminder = task.getReminder();
            TaskReminder reminderToPersist;

            if (incomingReminder.getId() != null) {

                reminderToPersist = taskReminderRepository.findById(incomingReminder.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Reminder not found"));

                // Alanları güncellemek icin
                validateReminder(incomingReminder);

                reminderToPersist.setDate(incomingReminder.getDate());
                reminderToPersist.setTime(incomingReminder.getTime());
                reminderToPersist.setDateEnabled(incomingReminder.isDateEnabled());
                reminderToPersist.setTimeEnabled(incomingReminder.isTimeEnabled());
                reminderToPersist.setRepeatType(incomingReminder.getRepeatType());

            } else {
                // İlk defa oluşturuluyorsa
                reminderToPersist = incomingReminder;
            }

            existingTask.setReminder(reminderToPersist);
        } else {
            existingTask.setReminder(null); // reminder null geldiyse ilişkisini kopar
        }

        return taskRepository.save(existingTask);

    }
    private void validateReminder(TaskReminder reminder) {
        if (reminder.isTimeEnabled() && !reminder.isDateEnabled()) {
            throw new IllegalArgumentException("Saat seçiliyse tarih de seçilmiş olmalı.");
        }

        if (reminder.isDateEnabled() && reminder.getDate() == null) {
            throw new IllegalArgumentException("Tarih seçiliyse bir tarih atanmalı.");
        }

        if (reminder.isTimeEnabled() && reminder.getTime() == null) {
            throw new IllegalArgumentException("Saat seçiliyse bir saat atanmalı.");
        }

        if ((reminder.isDateEnabled() || reminder.isTimeEnabled()) && reminder.getRepeatType() == null) {
            throw new IllegalArgumentException("Yineleme tipi boş olamaz.");
        }

        if (!reminder.isDateEnabled() && !reminder.isTimeEnabled() && reminder.getRepeatType() != null) {
            throw new IllegalArgumentException("Tarih ve saat kapalıyken tekrar seçilemez.");
        }
    }


    @Transactional
    @Override
    public void deleteTask(UUID taskListId, UUID taskId) {
        taskRepository.deleteByTaskListIdAndId(taskListId,taskId);
    }
/*      --- NOTE ABOUT TRANSACTIONAL ANNOTATION

Eğer bir JPA repository üzerinde özel bir silme (delete) işlemi yapıyorsam,
bu işlem bir transaction içinde olmak zorundadır.
deleteById gibi default metodlar zaten @Transactional ile tanımlı gelir,
fakat kendi yazdığım silme metodlarında bunu manuel olarak servis metodunun üstüne
@Transactional yazarak belirtmeliyim. Aksi halde TransactionRequiredException hatası alırım.

------------------ --------------------       -------------

 ne zaman @Transactional zorunlu olur?
Eğer bu metot içinde:

- Aynı anda birden fazla veritabanı işlemi yapılıyorsa (örneğin önce silip sonra eklemek gibi),
- Custom özel bir repository metodu çağrılıyorsa (örneğin deleteByTaskListIdAndId(...) gibi),
- Ya da bu işlem başarısız olursa rollback (geri alma) işlemi gerekliyse

👉 O zaman @Transactional yazılması gerekir.

Transaction nedir?
Bir transaction (işlem bloğu), veritabanına yapılan işlemlerin tamamının ya hep ya hiç yapılmasını garanti altına alır.

Yani işlemler başarılıysa hepsi yapılır, bir hata çıkarsa hiçbiri yapılmaz – sanki hiç başlamamış gibi.

🎯 Örnek:

@Transactional
public void transferMoney(Account from, Account to, int amount) {
    from.withdraw(amount);   // Hesaptan para çek
    to.deposit(amount);      // Diğer hesaba para yatır
}
 Eğer ilk satır başarılı ama ikinci satırda hata olursa:

  -withdraw() işlemi geri alınır (rollback yapılır).
  -Para çekilmemiş gibi olur.
   ->->->Böylece sistem tutarsız duruma düşmez.

    ! Transaction Kapsamında Olmak Ne Demek?
Bir metot transaction kapsamındaysa:

+ O metodun içindeki veritabanı işlemleri tek bir bütün gibi çalışır.
+ Hata olursa geriye alınır (rollback).
+ Başarılıysa veritabanına kalıcı olarak yazılır (commit).

   ! Spring'de @Transactional ne işe yarar?
Spring'de bir metoda @Transactional yazarsan, o metod:

Otomatik olarak bir transaction başlatır.

-->İçindeki işlemler başarılıysa → commit
-->Hata olursa → rollback

📌 Ne zaman gerekli?
--> Birden fazla veritabanı işlemi varsa
--> Veri tutarlılığı çok önemliyse
--> Özel repository metotları kullanıyorsan
--> Silme, güncelleme ve kayıt işlemleri bir aradaysa

🔁 Özet:
Transaction kapsamında çalışmak, işlemlerin:

Ya tamamen yapılması Ya da hiçbirinin yapılmaması demektir.

Spring'de @Transactional ile bunu kontrol edersin.

 */


}

