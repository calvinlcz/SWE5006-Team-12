package nusiss.swe5006.team12.todolist.web.rest;

import static nusiss.swe5006.team12.todolist.domain.RegisteredAsserts.*;
import static nusiss.swe5006.team12.todolist.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import nusiss.swe5006.team12.todolist.IntegrationTest;
import nusiss.swe5006.team12.todolist.domain.Registered;
import nusiss.swe5006.team12.todolist.repository.RegisteredRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RegisteredResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegisteredResourceIT {

    private static final String DEFAULT_ACCOUNT_NO = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_WORKSPACE = "AAAAAAAAAA";
    private static final String UPDATED_LINK_WORKSPACE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/registereds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RegisteredRepository registeredRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegisteredMockMvc;

    private Registered registered;

    private Registered insertedRegistered;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registered createEntity() {
        return new Registered().accountNo(DEFAULT_ACCOUNT_NO).linkWorkspace(DEFAULT_LINK_WORKSPACE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registered createUpdatedEntity() {
        return new Registered().accountNo(UPDATED_ACCOUNT_NO).linkWorkspace(UPDATED_LINK_WORKSPACE);
    }

    @BeforeEach
    public void initTest() {
        registered = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRegistered != null) {
            registeredRepository.delete(insertedRegistered);
            insertedRegistered = null;
        }
    }

    @Test
    @Transactional
    void createRegistered() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Registered
        var returnedRegistered = om.readValue(
            restRegisteredMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registered)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Registered.class
        );

        // Validate the Registered in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRegisteredUpdatableFieldsEquals(returnedRegistered, getPersistedRegistered(returnedRegistered));

        insertedRegistered = returnedRegistered;
    }

    @Test
    @Transactional
    void createRegisteredWithExistingId() throws Exception {
        // Create the Registered with an existing ID
        registered.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegisteredMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registered)))
            .andExpect(status().isBadRequest());

        // Validate the Registered in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRegistereds() throws Exception {
        // Initialize the database
        insertedRegistered = registeredRepository.saveAndFlush(registered);

        // Get all the registeredList
        restRegisteredMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registered.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO)))
            .andExpect(jsonPath("$.[*].linkWorkspace").value(hasItem(DEFAULT_LINK_WORKSPACE)));
    }

    @Test
    @Transactional
    void getRegistered() throws Exception {
        // Initialize the database
        insertedRegistered = registeredRepository.saveAndFlush(registered);

        // Get the registered
        restRegisteredMockMvc
            .perform(get(ENTITY_API_URL_ID, registered.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registered.getId().intValue()))
            .andExpect(jsonPath("$.accountNo").value(DEFAULT_ACCOUNT_NO))
            .andExpect(jsonPath("$.linkWorkspace").value(DEFAULT_LINK_WORKSPACE));
    }

    @Test
    @Transactional
    void getNonExistingRegistered() throws Exception {
        // Get the registered
        restRegisteredMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRegistered() throws Exception {
        // Initialize the database
        insertedRegistered = registeredRepository.saveAndFlush(registered);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the registered
        Registered updatedRegistered = registeredRepository.findById(registered.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRegistered are not directly saved in db
        em.detach(updatedRegistered);
        updatedRegistered.accountNo(UPDATED_ACCOUNT_NO).linkWorkspace(UPDATED_LINK_WORKSPACE);

        restRegisteredMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRegistered.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRegistered))
            )
            .andExpect(status().isOk());

        // Validate the Registered in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRegisteredToMatchAllProperties(updatedRegistered);
    }

    @Test
    @Transactional
    void putNonExistingRegistered() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registered.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegisteredMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registered.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registered))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registered in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegistered() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registered.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegisteredMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(registered))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registered in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegistered() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registered.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegisteredMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registered)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Registered in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegisteredWithPatch() throws Exception {
        // Initialize the database
        insertedRegistered = registeredRepository.saveAndFlush(registered);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the registered using partial update
        Registered partialUpdatedRegistered = new Registered();
        partialUpdatedRegistered.setId(registered.getId());

        partialUpdatedRegistered.accountNo(UPDATED_ACCOUNT_NO);

        restRegisteredMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistered.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRegistered))
            )
            .andExpect(status().isOk());

        // Validate the Registered in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRegisteredUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRegistered, registered),
            getPersistedRegistered(registered)
        );
    }

    @Test
    @Transactional
    void fullUpdateRegisteredWithPatch() throws Exception {
        // Initialize the database
        insertedRegistered = registeredRepository.saveAndFlush(registered);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the registered using partial update
        Registered partialUpdatedRegistered = new Registered();
        partialUpdatedRegistered.setId(registered.getId());

        partialUpdatedRegistered.accountNo(UPDATED_ACCOUNT_NO).linkWorkspace(UPDATED_LINK_WORKSPACE);

        restRegisteredMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistered.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRegistered))
            )
            .andExpect(status().isOk());

        // Validate the Registered in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRegisteredUpdatableFieldsEquals(partialUpdatedRegistered, getPersistedRegistered(partialUpdatedRegistered));
    }

    @Test
    @Transactional
    void patchNonExistingRegistered() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registered.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegisteredMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, registered.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(registered))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registered in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegistered() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registered.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegisteredMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(registered))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registered in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegistered() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registered.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegisteredMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(registered)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Registered in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegistered() throws Exception {
        // Initialize the database
        insertedRegistered = registeredRepository.saveAndFlush(registered);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the registered
        restRegisteredMockMvc
            .perform(delete(ENTITY_API_URL_ID, registered.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return registeredRepository.count();
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

    protected Registered getPersistedRegistered(Registered registered) {
        return registeredRepository.findById(registered.getId()).orElseThrow();
    }

    protected void assertPersistedRegisteredToMatchAllProperties(Registered expectedRegistered) {
        assertRegisteredAllPropertiesEquals(expectedRegistered, getPersistedRegistered(expectedRegistered));
    }

    protected void assertPersistedRegisteredToMatchUpdatableProperties(Registered expectedRegistered) {
        assertRegisteredAllUpdatablePropertiesEquals(expectedRegistered, getPersistedRegistered(expectedRegistered));
    }
}
