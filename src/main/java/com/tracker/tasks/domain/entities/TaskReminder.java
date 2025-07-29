package com.tracker.tasks.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class TaskReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private boolean isDateEnabled;

    @Column(nullable = false)
    private boolean isTimeEnabled;


    private LocalDate date;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private ReminderRepeatType repeatType;

    @Column(nullable = false)
    private boolean enabled;


    @OneToOne
    private Task task;

    public TaskReminder() {
    }

    public TaskReminder(UUID id, boolean isDateEnabled, boolean isTimeEnabled, LocalDate date, LocalTime time, ReminderRepeatType repeatType, boolean enabled, Task task) {
        this.id = id;
        this.isDateEnabled = isDateEnabled;
        this.isTimeEnabled = isTimeEnabled;
        this.date = date;
        this.time = time;
        this.repeatType = repeatType;
        this.enabled = enabled;
        this.task = task;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isDateEnabled() {
        return isDateEnabled;
    }

    public void setDateEnabled(boolean dateEnabled) {
        isDateEnabled = dateEnabled;
    }

    public boolean isTimeEnabled() {
        return isTimeEnabled;
    }

    public void setTimeEnabled(boolean timeEnabled) {
        isTimeEnabled = timeEnabled;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ReminderRepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(ReminderRepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskReminder that = (TaskReminder) o;
        return isDateEnabled == that.isDateEnabled && isTimeEnabled == that.isTimeEnabled && enabled == that.enabled && Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(time, that.time) && repeatType == that.repeatType && Objects.equals(task, that.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isDateEnabled, isTimeEnabled, date, time, repeatType, enabled, task);
    }

    @Override
    public String toString() {
        return "TaskReminder{" +
                "id=" + id +
                ", isDateEnabled=" + isDateEnabled +
                ", isTimeEnabled=" + isTimeEnabled +
                ", date=" + date +
                ", time=" + time +
                ", repeatType=" + repeatType +
                ", enabled=" + enabled +
                ", task=" + task +
                '}';
    }
}
