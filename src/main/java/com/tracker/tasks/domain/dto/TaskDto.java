package com.tracker.tasks.domain.dto;
// record ne ise yarıyor bi bak , dto tanım yazdır


import com.tracker.tasks.domain.entities.TaskPriority;
import com.tracker.tasks.domain.entities.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority priority,
        TaskStatus status,
        TaskReminderDto reminder
) {
}
/*
   DTO'ları  record olarak tanımlanması en güncel kullanılan yöntem.
--> Java'da record sınıfları, sadece veri tutmak (data carrier) için tasarlanmış,
 immutable (değiştirilemez) veri yapılarıdır.

 :: Final'dır, yani alt sınıf oluşturulamaz.
 :: Alanları (field) otomatik olarak:
 -private final olur
 -Constructor otomatik tanımlanır
 -getter benzeri metodlar oluşur (alan ismiyle erişilir)
 -equals(), hashCode() ve toString() otomatik gelir
 -Setter yoktur → Immutable’dır (değiştirilemez)
 -->-->Yalnızca veri taşımak için kullanılır, business logic içermez.


 DTO = Data Transfer Object

Katmanlar arası veri taşımak için kullanılan sade nesnelerdir.

Genelde sadece veri taşır, iş mantığı içermez.
Kullandığım yapıda veri taşıma mantığında olduğu için JPA anotasyonlarına,
rel. gösterimlerine ihtiyacımız yok , bunlar entity classlarında yapıldı.
No taskList reference - we'll handle this relationship through URLs in our REST API
No JPA annotations - DTOs are simple data carriers
Controller ↔ Service ↔ Entity arasında kullanılır.
Amaç: Veritabanı Entity’sini dış dünyaya (frontend'e veya API’ye) direkt açmamak.
 */