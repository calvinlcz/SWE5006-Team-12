package nusiss.swe5006.team12.todolist.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import nusiss.swe5006.team12.todolist.domain.WorkSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class WorkSpaceRepositoryWithBagRelationshipsImpl implements WorkSpaceRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String WORKSPACES_PARAMETER = "workSpaces";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<WorkSpace> fetchBagRelationships(Optional<WorkSpace> workSpace) {
        return workSpace.map(this::fetchRegistereds);
    }

    @Override
    public Page<WorkSpace> fetchBagRelationships(Page<WorkSpace> workSpaces) {
        return new PageImpl<>(fetchBagRelationships(workSpaces.getContent()), workSpaces.getPageable(), workSpaces.getTotalElements());
    }

    @Override
    public List<WorkSpace> fetchBagRelationships(List<WorkSpace> workSpaces) {
        return Optional.of(workSpaces).map(this::fetchRegistereds).orElse(Collections.emptyList());
    }

    WorkSpace fetchRegistereds(WorkSpace result) {
        return entityManager
            .createQuery(
                "select workSpace from WorkSpace workSpace left join fetch workSpace.registereds where workSpace.id = :id",
                WorkSpace.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<WorkSpace> fetchRegistereds(List<WorkSpace> workSpaces) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, workSpaces.size()).forEach(index -> order.put(workSpaces.get(index).getId(), index));
        List<WorkSpace> result = entityManager
            .createQuery(
                "select workSpace from WorkSpace workSpace left join fetch workSpace.registereds where workSpace in :workSpaces",
                WorkSpace.class
            )
            .setParameter(WORKSPACES_PARAMETER, workSpaces)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
