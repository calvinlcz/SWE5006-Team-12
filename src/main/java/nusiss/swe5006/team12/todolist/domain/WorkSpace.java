package nusiss.swe5006.team12.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkSpace.
 */
@Entity
@Table(name = "work_space")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkSpace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "date_modified", nullable = false)
    private LocalDate dateModified;

    @NotNull
    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "shared_workspace")
    private Boolean sharedWorkspace;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workSpace")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "notifications", "workSpace" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_work_space__registered",
        joinColumns = @JoinColumn(name = "work_space_id"),
        inverseJoinColumns = @JoinColumn(name = "registered_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workSpaces" }, allowSetters = true)
    private Set<Registered> registereds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkSpace id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public WorkSpace name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    public WorkSpace dateCreated(LocalDate dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public WorkSpace createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public WorkSpace dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public WorkSpace modifiedBy(String modifiedBy) {
        this.setModifiedBy(modifiedBy);
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Boolean getSharedWorkspace() {
        return this.sharedWorkspace;
    }

    public WorkSpace sharedWorkspace(Boolean sharedWorkspace) {
        this.setSharedWorkspace(sharedWorkspace);
        return this;
    }

    public void setSharedWorkspace(Boolean sharedWorkspace) {
        this.sharedWorkspace = sharedWorkspace;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setWorkSpace(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setWorkSpace(this));
        }
        this.tasks = tasks;
    }

    public WorkSpace tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public WorkSpace addTask(Task task) {
        this.tasks.add(task);
        task.setWorkSpace(this);
        return this;
    }

    public WorkSpace removeTask(Task task) {
        this.tasks.remove(task);
        task.setWorkSpace(null);
        return this;
    }

    public Set<Registered> getRegistereds() {
        return this.registereds;
    }

    public void setRegistereds(Set<Registered> registereds) {
        this.registereds = registereds;
    }

    public WorkSpace registereds(Set<Registered> registereds) {
        this.setRegistereds(registereds);
        return this;
    }

    public WorkSpace addRegistered(Registered registered) {
        this.registereds.add(registered);
        return this;
    }

    public WorkSpace removeRegistered(Registered registered) {
        this.registereds.remove(registered);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkSpace)) {
            return false;
        }
        return getId() != null && getId().equals(((WorkSpace) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkSpace{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", sharedWorkspace='" + getSharedWorkspace() + "'" +
            "}";
    }
}
