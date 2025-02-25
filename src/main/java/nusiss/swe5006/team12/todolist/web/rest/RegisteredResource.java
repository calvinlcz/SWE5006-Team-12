package nusiss.swe5006.team12.todolist.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nusiss.swe5006.team12.todolist.domain.Registered;
import nusiss.swe5006.team12.todolist.repository.RegisteredRepository;
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
 * REST controller for managing {@link nusiss.swe5006.team12.todolist.domain.Registered}.
 */
@RestController
@RequestMapping("/api/registereds")
@Transactional
public class RegisteredResource {

    private static final Logger LOG = LoggerFactory.getLogger(RegisteredResource.class);

    private static final String ENTITY_NAME = "registered";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegisteredRepository registeredRepository;

    public RegisteredResource(RegisteredRepository registeredRepository) {
        this.registeredRepository = registeredRepository;
    }

    /**
     * {@code POST  /registereds} : Create a new registered.
     *
     * @param registered the registered to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registered, or with status {@code 400 (Bad Request)} if the registered has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Registered> createRegistered(@RequestBody Registered registered) throws URISyntaxException {
        LOG.debug("REST request to save Registered : {}", registered);
        if (registered.getId() != null) {
            throw new BadRequestAlertException("A new registered cannot already have an ID", ENTITY_NAME, "idexists");
        }
        registered = registeredRepository.save(registered);
        return ResponseEntity.created(new URI("/api/registereds/" + registered.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, registered.getId().toString()))
            .body(registered);
    }

    /**
     * {@code PUT  /registereds/:id} : Updates an existing registered.
     *
     * @param id the id of the registered to save.
     * @param registered the registered to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registered,
     * or with status {@code 400 (Bad Request)} if the registered is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registered couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Registered> updateRegistered(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Registered registered
    ) throws URISyntaxException {
        LOG.debug("REST request to update Registered : {}, {}", id, registered);
        if (registered.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registered.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registeredRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        registered = registeredRepository.save(registered);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registered.getId().toString()))
            .body(registered);
    }

    /**
     * {@code PATCH  /registereds/:id} : Partial updates given fields of an existing registered, field will ignore if it is null
     *
     * @param id the id of the registered to save.
     * @param registered the registered to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registered,
     * or with status {@code 400 (Bad Request)} if the registered is not valid,
     * or with status {@code 404 (Not Found)} if the registered is not found,
     * or with status {@code 500 (Internal Server Error)} if the registered couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Registered> partialUpdateRegistered(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Registered registered
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Registered partially : {}, {}", id, registered);
        if (registered.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registered.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registeredRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Registered> result = registeredRepository
            .findById(registered.getId())
            .map(existingRegistered -> {
                if (registered.getAccountNo() != null) {
                    existingRegistered.setAccountNo(registered.getAccountNo());
                }
                if (registered.getLinkWorkspace() != null) {
                    existingRegistered.setLinkWorkspace(registered.getLinkWorkspace());
                }

                return existingRegistered;
            })
            .map(registeredRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registered.getId().toString())
        );
    }

    /**
     * {@code GET  /registereds} : get all the registereds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registereds in body.
     */
    @GetMapping("")
    public List<Registered> getAllRegistereds() {
        LOG.debug("REST request to get all Registereds");
        return registeredRepository.findAll();
    }

    /**
     * {@code GET  /registereds/:id} : get the "id" registered.
     *
     * @param id the id of the registered to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registered, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Registered> getRegistered(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Registered : {}", id);
        Optional<Registered> registered = registeredRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(registered);
    }

    /**
     * {@code DELETE  /registereds/:id} : delete the "id" registered.
     *
     * @param id the id of the registered to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistered(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Registered : {}", id);
        registeredRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
