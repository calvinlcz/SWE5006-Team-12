package nusiss.sew5006.team12.toodolist.domain.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class WorkSpaceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private LocalDate dateCreated;
    private String createdBy;
    private LocalDate dateModified;
    private String modifiedBy;
    private Boolean sharedWorkspace;
    private Set<TaskDTO> taskIds = new HashSet<>();
    private Set<RegisteredDTO> registeredIds = new HashSet<>();

    // Default constructor
    public WorkSpaceDTO() {}

    // Constructor with all fields
    public WorkSpaceDTO(
        Long id,
        String name,
        LocalDate dateCreated,
        String createdBy,
        LocalDate dateModified,
        String modifiedBy,
        Boolean sharedWorkspace,
        Set<TaskDTO> taskIds,
        Set<RegisteredDTO> registeredIds
    ) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.dateModified = dateModified;
        this.modifiedBy = modifiedBy;
        this.sharedWorkspace = sharedWorkspace;
        this.taskIds = taskIds;
        this.registeredIds = registeredIds;
    }

    // Getters and setters
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

    public Boolean getSharedWorkspace() {
        return sharedWorkspace;
    }

    public void setSharedWorkspace(Boolean sharedWorkspace) {
        this.sharedWorkspace = sharedWorkspace;
    }

    public Set<TaskDTO> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Set<TaskDTO> taskIds) {
        this.taskIds = taskIds;
    }

    public Set<RegisteredDTO> getRegisteredIds() {
        return registeredIds;
    }

    public void setRegisteredIds(Set<RegisteredDTO> registeredIds) {
        this.registeredIds = registeredIds;
    }
}
