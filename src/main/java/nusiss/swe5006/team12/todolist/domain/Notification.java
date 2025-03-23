package nusiss.swe5006.team12.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "message", length = 50, nullable = false)
    private String message;

    @NotNull
    @Column(name = "alert_time", nullable = false)
    private LocalDate alertTime;

    @Column(name = "disable_notification")
    private Boolean disableNotification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "notifications", "workSpace" }, allowSetters = true)
    private Task task;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getAlertTime() {
        return this.alertTime;
    }

    public Notification alertTime(LocalDate alertTime) {
        this.setAlertTime(alertTime);
        return this;
    }

    public void setAlertTime(LocalDate alertTime) {
        this.alertTime = alertTime;
    }

    public Boolean getDisableNotification() {
        return this.disableNotification;
    }

    public Notification disableNotification(Boolean disableNotification) {
        this.setDisableNotification(disableNotification);
        return this;
    }

    public void setDisableNotification(Boolean disableNotification) {
        this.disableNotification = disableNotification;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Notification task(Task task) {
        this.setTask(task);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", alertTime='" + getAlertTime() + "'" +
            ", disableNotification='" + getDisableNotification() + "'" +
            "}";
    }
}
