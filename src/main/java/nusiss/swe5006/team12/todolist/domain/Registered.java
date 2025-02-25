package nusiss.swe5006.team12.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Registered.
 */
@Entity
@Table(name = "registered")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Registered implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "link_workspace")
    private String linkWorkspace;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "registereds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks", "registereds" }, allowSetters = true)
    private Set<WorkSpace> workSpaces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Registered id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public Registered accountNo(String accountNo) {
        this.setAccountNo(accountNo);
        return this;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getLinkWorkspace() {
        return this.linkWorkspace;
    }

    public Registered linkWorkspace(String linkWorkspace) {
        this.setLinkWorkspace(linkWorkspace);
        return this;
    }

    public void setLinkWorkspace(String linkWorkspace) {
        this.linkWorkspace = linkWorkspace;
    }

    public Set<WorkSpace> getWorkSpaces() {
        return this.workSpaces;
    }

    public void setWorkSpaces(Set<WorkSpace> workSpaces) {
        if (this.workSpaces != null) {
            this.workSpaces.forEach(i -> i.removeRegistered(this));
        }
        if (workSpaces != null) {
            workSpaces.forEach(i -> i.addRegistered(this));
        }
        this.workSpaces = workSpaces;
    }

    public Registered workSpaces(Set<WorkSpace> workSpaces) {
        this.setWorkSpaces(workSpaces);
        return this;
    }

    public Registered addWorkSpace(WorkSpace workSpace) {
        this.workSpaces.add(workSpace);
        workSpace.getRegistereds().add(this);
        return this;
    }

    public Registered removeWorkSpace(WorkSpace workSpace) {
        this.workSpaces.remove(workSpace);
        workSpace.getRegistereds().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Registered)) {
            return false;
        }
        return getId() != null && getId().equals(((Registered) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Registered{" +
            "id=" + getId() +
            ", accountNo='" + getAccountNo() + "'" +
            ", linkWorkspace='" + getLinkWorkspace() + "'" +
            "}";
    }
}
