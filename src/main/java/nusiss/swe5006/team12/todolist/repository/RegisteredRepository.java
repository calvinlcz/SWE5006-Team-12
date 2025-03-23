package nusiss.swe5006.team12.todolist.repository;

import nusiss.swe5006.team12.todolist.domain.Registered;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Registered entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegisteredRepository extends JpaRepository<Registered, Long> {}
