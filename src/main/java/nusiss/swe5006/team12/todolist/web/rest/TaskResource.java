package nusiss.swe5006.team12.todolist.web.rest;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nusiss.swe5006.team12.todolist.domain.Task;
import nusiss.swe5006.team12.todolist.repository.TaskRepository;
import nusiss.swe5006.team12.todolist.service.GoogleOAuth2Service;
import nusiss.swe5006.team12.todolist.service.IOAuth2StrategyService;
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
 * REST controller for managing {@link nusiss.swe5006.team12.todolist.domain.Task}.
 */
@RestController
@RequestMapping("/api/tasks")
@Transactional
public class TaskResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskRepository taskRepository;

    public TaskResource(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * {@code POST  /tasks} : Create a new task.
     *
     * @param task the task to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new task, or with status {@code 400 (Bad Request)} if the task has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) throws URISyntaxException {
        LOG.debug("REST request to save Task : {}", task);
        if (task.getId() != null) {
            throw new BadRequestAlertException("A new task cannot already have an ID", ENTITY_NAME, "idexists");
        }
        task = taskRepository.save(task);
        IOAuth2StrategyService service = new GoogleOAuth2Service();

        return ResponseEntity.created(new URI("/api/tasks/" + task.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, task.getId().toString()))
            .body(task);
    }

    /**
     * {@code PUT  /tasks/:id} : Updates an existing task.
     *
     * @param id the id of the task to save.
     * @param task the task to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated task,
     * or with status {@code 400 (Bad Request)} if the task is not valid,
     * or with status {@code 500 (Internal Server Error)} if the task couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Task task)
        throws URISyntaxException {
        LOG.debug("REST request to update Task : {}, {}", id, task);
        if (task.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, task.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        task = taskRepository.save(task);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, task.getId().toString()))
            .body(task);
    }

    @PostMapping("/addToGoogleCalendar/{id}")
    public ResponseEntity<Task> addToGoogleCalendar(@PathVariable(value = "id", required = false) final Long id) throws Exception {
        LOG.debug("REST request to update Task : {}", id);

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            IOAuth2StrategyService service = new GoogleOAuth2Service();
            service.addEvent(task.orElseThrow());
        }
        return ResponseUtil.wrapOrNotFound(task);
    }

    /**
     * {@code PATCH  /tasks/:id} : Partial updates given fields of an existing task, field will ignore if it is null
     *
     * @param id the id of the task to save.
     * @param task the task to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated task,
     * or with status {@code 400 (Bad Request)} if the task is not valid,
     * or with status {@code 404 (Not Found)} if the task is not found,
     * or with status {@code 500 (Internal Server Error)} if the task couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Task> partialUpdateTask(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Task task
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Task partially : {}, {}", id, task);
        if (task.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, task.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Task> result = taskRepository
            .findById(task.getId())
            .map(existingTask -> {
                if (task.getName() != null) {
                    existingTask.setName(task.getName());
                }
                if (task.getDescription() != null) {
                    existingTask.setDescription(task.getDescription());
                }
                if (task.getDueDate() != null) {
                    existingTask.setDueDate(task.getDueDate());
                }
                if (task.getDateCreated() != null) {
                    existingTask.setDateCreated(task.getDateCreated());
                }
                if (task.getCreatedBy() != null) {
                    existingTask.setCreatedBy(task.getCreatedBy());
                }
                if (task.getDateModified() != null) {
                    existingTask.setDateModified(task.getDateModified());
                }
                if (task.getModifiedBy() != null) {
                    existingTask.setModifiedBy(task.getModifiedBy());
                }
                if (task.getIsRecurring() != null) {
                    existingTask.setIsRecurring(task.getIsRecurring());
                }
                if (task.getRecurrenceRule() != null) {
                    existingTask.setRecurrenceRule(task.getRecurrenceRule());
                }
                if (task.getPriority() != null) {
                    existingTask.setPriority(task.getPriority());
                }
                if (task.getStatus() != null) {
                    existingTask.setStatus(task.getStatus());
                }

                return existingTask;
            })
            .map(taskRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, task.getId().toString())
        );
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tasks in body.
     */
    @GetMapping("")
    public List<Task> getAllTasks() {
        LOG.debug("REST request to get all Tasks");
        return taskRepository.findAll();
    }

    @GetMapping("/search")
    public List<Task> searchForTasks(@RequestParam String keyword) {
        LOG.debug("REST request to search Tasks by name");
        return taskRepository.findByNameContaining(keyword);
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" task.
     *
     * @param id the id of the task to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the task, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Task : {}", id);
        Optional<Task> task = taskRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(task);
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" task.
     *
     * @param id the id of the task to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Task : {}", id);
        taskRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
