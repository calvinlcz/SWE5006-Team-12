package nusiss.sew5006.team12.toodolist.domain.dto;

import java.time.LocalDate;

public class NotificationDTO {

    private Long id;

    private String message;

    private LocalDate alertTime;

    private Boolean disableNotification;

    private Long taskId; // Task ID to link to the task. You can add more details if needed.

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(LocalDate alertTime) {
        this.alertTime = alertTime;
    }

    public Boolean getDisableNotification() {
        return disableNotification;
    }

    public void setDisableNotification(Boolean disableNotification) {
        this.disableNotification = disableNotification;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    // Constructors, equals, hashcode methods can also be added as needed.
}
