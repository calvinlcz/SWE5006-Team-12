package nusiss.swe5006.team12.todolist.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nusiss.swe5006.team12.todolist.domain.WorkSpace;
import nusiss.swe5006.team12.todolist.repository.WorkSpaceRepository;
import nusiss.swe5006.team12.todolist.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link nusiss.swe5006.team12.todolist.domain.WorkSpace}.
 */
@RestController
@RequestMapping("/api/work-spaces")
@Transactional
public class WorkSpaceResource {

    private static final Logger LOG = LoggerFactory.getLogger(WorkSpaceResource.class);

    private static final String ENTITY_NAME = "workSpace";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkSpaceRepository workSpaceRepository;

    public WorkSpaceResource(WorkSpaceRepository workSpaceRepository) {
        this.workSpaceRepository = workSpaceRepository;
    }

    /**
     * {@code POST  /work-spaces} : Create a new workSpace.
     *
     * @param workSpace the workSpace to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workSpace, or with status {@code 400 (Bad Request)} if the workSpace has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WorkSpace> createWorkSpace(@Valid @RequestBody WorkSpace workSpace) throws URISyntaxException {
        LOG.debug("REST request to save WorkSpace : {}", workSpace);
        if (workSpace.getId() != null) {
            throw new BadRequestAlertException("A new workSpace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        workSpace = workSpaceRepository.save(workSpace);
        return ResponseEntity.created(new URI("/api/work-spaces/" + workSpace.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, workSpace.getId().toString()))
            .body(workSpace);
    }

    /**
     * {@code PUT  /work-spaces/:id} : Updates an existing workSpace.
     *
     * @param id the id of the workSpace to save.
     * @param workSpace the workSpace to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workSpace,
     * or with status {@code 400 (Bad Request)} if the workSpace is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workSpace couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkSpace> updateWorkSpace(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkSpace workSpace
    ) throws URISyntaxException {
        LOG.debug("REST request to update WorkSpace : {}, {}", id, workSpace);
        if (workSpace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workSpace.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workSpaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        workSpace = workSpaceRepository.save(workSpace);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workSpace.getId().toString()))
            .body(workSpace);
    }

    /**
     * {@code PATCH  /work-spaces/:id} : Partial updates given fields of an existing workSpace, field will ignore if it is null
     *
     * @param id the id of the workSpace to save.
     * @param workSpace the workSpace to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workSpace,
     * or with status {@code 400 (Bad Request)} if the workSpace is not valid,
     * or with status {@code 404 (Not Found)} if the workSpace is not found,
     * or with status {@code 500 (Internal Server Error)} if the workSpace couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkSpace> partialUpdateWorkSpace(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkSpace workSpace
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WorkSpace partially : {}, {}", id, workSpace);
        if (workSpace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workSpace.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workSpaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkSpace> result = workSpaceRepository
            .findById(workSpace.getId())
            .map(existingWorkSpace -> {
                if (workSpace.getName() != null) {
                    existingWorkSpace.setName(workSpace.getName());
                }
                if (workSpace.getDateCreated() != null) {
                    existingWorkSpace.setDateCreated(workSpace.getDateCreated());
                }
                if (workSpace.getCreatedBy() != null) {
                    existingWorkSpace.setCreatedBy(workSpace.getCreatedBy());
                }
                if (workSpace.getDateModified() != null) {
                    existingWorkSpace.setDateModified(workSpace.getDateModified());
                }
                if (workSpace.getModifiedBy() != null) {
                    existingWorkSpace.setModifiedBy(workSpace.getModifiedBy());
                }
                if (workSpace.getSharedWorkspace() != null) {
                    existingWorkSpace.setSharedWorkspace(workSpace.getSharedWorkspace());
                }

                return existingWorkSpace;
            })
            .map(workSpaceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workSpace.getId().toString())
        );
    }

    /**
     * {@code GET  /work-spaces} : get all the workSpaces.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workSpaces in body.
     */
    @GetMapping("")
    public List<WorkSpace> getAllWorkSpaces(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all WorkSpaces");
        if (eagerload) {
            return workSpaceRepository.findAllWithEagerRelationships();
        } else {
            return workSpaceRepository.findAll();
        }
    }

    /**
     * {@code GET  /work-spaces/:id} : get the "id" workSpace.
     *
     * @param id the id of the workSpace to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workSpace, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkSpace> getWorkSpace(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WorkSpace : {}", id);
        Optional<WorkSpace> workSpace = workSpaceRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(workSpace);
    }

    /**
     * {@code DELETE  /work-spaces/:id} : delete the "id" workSpace.
     *
     * @param id the id of the workSpace to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkSpace(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WorkSpace : {}", id);
        workSpaceRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
