package nusiss.swe5006.team12.todolist.web.rest;

import static nusiss.swe5006.team12.todolist.domain.WorkSpaceAsserts.*;
import static nusiss.swe5006.team12.todolist.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import nusiss.swe5006.team12.todolist.IntegrationTest;
import nusiss.swe5006.team12.todolist.domain.WorkSpace;
import nusiss.swe5006.team12.todolist.repository.WorkSpaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkSpaceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkSpaceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHARED_WORKSPACE = false;
    private static final Boolean UPDATED_SHARED_WORKSPACE = true;

    private static final String ENTITY_API_URL = "/api/work-spaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Mock
    private WorkSpaceRepository workSpaceRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkSpaceMockMvc;

    private WorkSpace workSpace;

    private WorkSpace insertedWorkSpace;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkSpace createEntity() {
        return new WorkSpace()
            .name(DEFAULT_NAME)
            .dateCreated(DEFAULT_DATE_CREATED)
            .createdBy(DEFAULT_CREATED_BY)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .sharedWorkspace(DEFAULT_SHARED_WORKSPACE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkSpace createUpdatedEntity() {
        return new WorkSpace()
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .dateModified(UPDATED_DATE_MODIFIED)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .sharedWorkspace(UPDATED_SHARED_WORKSPACE);
    }

    @BeforeEach
    public void initTest() {
        workSpace = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWorkSpace != null) {
            workSpaceRepository.delete(insertedWorkSpace);
            insertedWorkSpace = null;
        }
    }

    @Test
    @Transactional
    void createWorkSpace() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WorkSpace
        var returnedWorkSpace = om.readValue(
            restWorkSpaceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workSpace)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkSpace.class
        );

        // Validate the WorkSpace in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWorkSpaceUpdatableFieldsEquals(returnedWorkSpace, getPersistedWorkSpace(returnedWorkSpace));

        insertedWorkSpace = returnedWorkSpace;
    }

    @Test
    @Transactional
    void createWorkSpaceWithExistingId() throws Exception {
        // Create the WorkSpace with an existing ID
        workSpace.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workSpace)))
            .andExpect(status().isBadRequest());

        // Validate the WorkSpace in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workSpace.setName(null);

        // Create the WorkSpace, which fails.

        restWorkSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workSpace)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workSpace.setDateCreated(null);

        // Create the WorkSpace, which fails.

        restWorkSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workSpace)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workSpace.setCreatedBy(null);

        // Create the WorkSpace, which fails.

        restWorkSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workSpace)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workSpace.setDateModified(null);

        // Create the WorkSpace, which fails.

        restWorkSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workSpace)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workSpace.setModifiedBy(null);

        // Create the WorkSpace, which fails.

        restWorkSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workSpace)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkSpaces() throws Exception {
        // Initialize the database
        insertedWorkSpace = workSpaceRepository.saveAndFlush(workSpace);

        // Get all the workSpaceList
        restWorkSpaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workSpace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].sharedWorkspace").value(hasItem(DEFAULT_SHARED_WORKSPACE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkSpacesWithEagerRelationshipsIsEnabled() throws Exception {
        when(workSpaceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkSpaceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workSpaceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkSpacesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workSpaceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkSpaceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(workSpaceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWorkSpace() throws Exception {
        // Initialize the database
        insertedWorkSpace = workSpaceRepository.saveAndFlush(workSpace);

        // Get the workSpace
        restWorkSpaceMockMvc
            .perform(get(ENTITY_API_URL_ID, workSpace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workSpace.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.sharedWorkspace").value(DEFAULT_SHARED_WORKSPACE));
    }

    @Test
    @Transactional
    void getNonExistingWorkSpace() throws Exception {
        // Get the workSpace
        restWorkSpaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkSpace() throws Exception {
        // Initialize the database
        insertedWorkSpace = workSpaceRepository.saveAndFlush(workSpace);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workSpace
        WorkSpace updatedWorkSpace = workSpaceRepository.findById(workSpace.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorkSpace are not directly saved in db
        em.detach(updatedWorkSpace);
        updatedWorkSpace
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .dateModified(UPDATED_DATE_MODIFIED)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .sharedWorkspace(UPDATED_SHARED_WORKSPACE);

        restWorkSpaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkSpace.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWorkSpace))
            )
            .andExpect(status().isOk());

        // Validate the WorkSpace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkSpaceToMatchAllProperties(updatedWorkSpace);
    }

    @Test
    @Transactional
    void putNonExistingWorkSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workSpace.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkSpaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workSpace.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workSpace))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkSpace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workSpace.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkSpaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workSpace))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkSpace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workSpace.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkSpaceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workSpace)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkSpace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkSpaceWithPatch() throws Exception {
        // Initialize the database
        insertedWorkSpace = workSpaceRepository.saveAndFlush(workSpace);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workSpace using partial update
        WorkSpace partialUpdatedWorkSpace = new WorkSpace();
        partialUpdatedWorkSpace.setId(workSpace.getId());

        partialUpdatedWorkSpace.name(UPDATED_NAME).dateModified(UPDATED_DATE_MODIFIED).modifiedBy(UPDATED_MODIFIED_BY);

        restWorkSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkSpace.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkSpace))
            )
            .andExpect(status().isOk());

        // Validate the WorkSpace in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkSpaceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorkSpace, workSpace),
            getPersistedWorkSpace(workSpace)
        );
    }

    @Test
    @Transactional
    void fullUpdateWorkSpaceWithPatch() throws Exception {
        // Initialize the database
        insertedWorkSpace = workSpaceRepository.saveAndFlush(workSpace);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workSpace using partial update
        WorkSpace partialUpdatedWorkSpace = new WorkSpace();
        partialUpdatedWorkSpace.setId(workSpace.getId());

        partialUpdatedWorkSpace
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .dateModified(UPDATED_DATE_MODIFIED)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .sharedWorkspace(UPDATED_SHARED_WORKSPACE);

        restWorkSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkSpace.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkSpace))
            )
            .andExpect(status().isOk());

        // Validate the WorkSpace in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkSpaceUpdatableFieldsEquals(partialUpdatedWorkSpace, getPersistedWorkSpace(partialUpdatedWorkSpace));
    }

    @Test
    @Transactional
    void patchNonExistingWorkSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workSpace.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workSpace.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workSpace))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkSpace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workSpace.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workSpace))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkSpace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workSpace.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkSpaceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workSpace)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkSpace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkSpace() throws Exception {
        // Initialize the database
        insertedWorkSpace = workSpaceRepository.saveAndFlush(workSpace);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the workSpace
        restWorkSpaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, workSpace.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return workSpaceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected WorkSpace getPersistedWorkSpace(WorkSpace workSpace) {
        return workSpaceRepository.findById(workSpace.getId()).orElseThrow();
    }

    protected void assertPersistedWorkSpaceToMatchAllProperties(WorkSpace expectedWorkSpace) {
        assertWorkSpaceAllPropertiesEquals(expectedWorkSpace, getPersistedWorkSpace(expectedWorkSpace));
    }

    protected void assertPersistedWorkSpaceToMatchUpdatableProperties(WorkSpace expectedWorkSpace) {
        assertWorkSpaceAllUpdatablePropertiesEquals(expectedWorkSpace, getPersistedWorkSpace(expectedWorkSpace));
    }
}
