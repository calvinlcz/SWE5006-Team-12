package nusiss.sew5006.team12.toodolist.domain.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RegisteredDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String accountNo;
    private String linkWorkspace;
    private Set<WorkSpaceDTO> workSpaces = new HashSet<>(); // Only store IDs of WorkSpaces

    // Default constructor
    public RegisteredDTO() {}

    // Constructor with all fields
    public RegisteredDTO(Long id, String accountNo, String linkWorkspace, Set<WorkSpaceDTO> workSpaces) {
        this.id = id;
        this.accountNo = accountNo;
        this.linkWorkspace = linkWorkspace;
        this.workSpaces = workSpaces;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getLinkWorkspace() {
        return linkWorkspace;
    }

    public void setLinkWorkspace(String linkWorkspace) {
        this.linkWorkspace = linkWorkspace;
    }

    public Set<WorkSpaceDTO> getWorkSpaceIds() {
        return workSpaces;
    }

    public void setWorkSpaceIds(Set<WorkSpaceDTO> workSpaces) {
        this.workSpaces = workSpaces;
    }
}
