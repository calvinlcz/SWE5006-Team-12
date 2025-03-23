package nusiss.swe5006.team12.todolist.repository;

import java.util.List;
import java.util.Optional;
import nusiss.swe5006.team12.todolist.domain.WorkSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkSpace entity.
 *
 * When extending this class, extend WorkSpaceRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface WorkSpaceRepository extends WorkSpaceRepositoryWithBagRelationships, JpaRepository<WorkSpace, Long> {
    default Optional<WorkSpace> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<WorkSpace> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<WorkSpace> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
