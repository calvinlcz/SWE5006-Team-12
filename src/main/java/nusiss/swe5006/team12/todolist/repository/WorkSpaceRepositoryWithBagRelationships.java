package nusiss.swe5006.team12.todolist.repository;

import java.util.List;
import java.util.Optional;
import nusiss.swe5006.team12.todolist.domain.WorkSpace;
import org.springframework.data.domain.Page;

public interface WorkSpaceRepositoryWithBagRelationships {
    Optional<WorkSpace> fetchBagRelationships(Optional<WorkSpace> workSpace);

    List<WorkSpace> fetchBagRelationships(List<WorkSpace> workSpaces);

    Page<WorkSpace> fetchBagRelationships(Page<WorkSpace> workSpaces);
}
