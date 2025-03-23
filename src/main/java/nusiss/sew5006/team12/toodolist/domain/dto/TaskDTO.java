package nusiss.sew5006.team12.toodolist.domain.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import nusiss.swe5006.team12.todolist.domain.enumeration.Rule;
import nusiss.swe5006.team12.todolist.domain.enumeration.Status;

public class TaskDTO {

    private Long id;

    private String name;

    private String description;

    private LocalDate dueDate;

    private LocalDate dateCreated;

    private String createdBy;

    private LocalDate dateModified;

    private String modifiedBy;

    private Boolean isRecurring;

    private Rule recurrenceRule;

    private String priority;

    private Status status;

    private Set<NotificationDTO> notifications = new HashSet<>();

    private Long workSpaceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Rule getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(Rule recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    public Long getWorkSpaceId() {
        return workSpaceId;
    }

    public void setWorkSpaceId(Long workSpaceId) {
        this.workSpaceId = workSpaceId;
    }
}
