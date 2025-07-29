package com.tracker.tasks.domain.entities;

public enum ReminderRepeatType {
    NEVER,         // Asla
    DAILY,         // Her Gün
    WEEKDAYS,      // Hafta İçi
    WEEKENDS,      // Hafta Sonları
    WEEKLY,        // Her Hafta
    BIWEEKLY,      // Her 2 Haftada Bir
    MONTHLY,       // Her Ay
    QUARTERLY,     // Her 3 Ayda Bir
    SEMI_ANNUALLY, // Her 6 Ayda Bir
    YEARLY         // Her Yıl
}
