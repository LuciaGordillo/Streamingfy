package com.streamingfy.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.streamingfy.app.IntegrationTest;
import com.streamingfy.app.domain.Perfiles;
import com.streamingfy.app.repository.PerfilesRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PerfilesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PerfilesResourceIT {

    private static final String DEFAULT_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEN = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/perfiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PerfilesRepository perfilesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerfilesMockMvc;

    private Perfiles perfiles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Perfiles createEntity(EntityManager em) {
        Perfiles perfiles = new Perfiles().imagen(DEFAULT_IMAGEN).nombre(DEFAULT_NOMBRE);
        return perfiles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Perfiles createUpdatedEntity(EntityManager em) {
        Perfiles perfiles = new Perfiles().imagen(UPDATED_IMAGEN).nombre(UPDATED_NOMBRE);
        return perfiles;
    }

    @BeforeEach
    public void initTest() {
        perfiles = createEntity(em);
    }

    @Test
    @Transactional
    void createPerfiles() throws Exception {
        int databaseSizeBeforeCreate = perfilesRepository.findAll().size();
        // Create the Perfiles
        restPerfilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfiles)))
            .andExpect(status().isCreated());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeCreate + 1);
        Perfiles testPerfiles = perfilesList.get(perfilesList.size() - 1);
        assertThat(testPerfiles.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testPerfiles.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createPerfilesWithExistingId() throws Exception {
        // Create the Perfiles with an existing ID
        perfiles.setId(1L);

        int databaseSizeBeforeCreate = perfilesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerfilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfiles)))
            .andExpect(status().isBadRequest());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPerfiles() throws Exception {
        // Initialize the database
        perfilesRepository.saveAndFlush(perfiles);

        // Get all the perfilesList
        restPerfilesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfiles.getId().intValue())))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getPerfiles() throws Exception {
        // Initialize the database
        perfilesRepository.saveAndFlush(perfiles);

        // Get the perfiles
        restPerfilesMockMvc
            .perform(get(ENTITY_API_URL_ID, perfiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(perfiles.getId().intValue()))
            .andExpect(jsonPath("$.imagen").value(DEFAULT_IMAGEN))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingPerfiles() throws Exception {
        // Get the perfiles
        restPerfilesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPerfiles() throws Exception {
        // Initialize the database
        perfilesRepository.saveAndFlush(perfiles);

        int databaseSizeBeforeUpdate = perfilesRepository.findAll().size();

        // Update the perfiles
        Perfiles updatedPerfiles = perfilesRepository.findById(perfiles.getId()).get();
        // Disconnect from session so that the updates on updatedPerfiles are not directly saved in db
        em.detach(updatedPerfiles);
        updatedPerfiles.imagen(UPDATED_IMAGEN).nombre(UPDATED_NOMBRE);

        restPerfilesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPerfiles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPerfiles))
            )
            .andExpect(status().isOk());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeUpdate);
        Perfiles testPerfiles = perfilesList.get(perfilesList.size() - 1);
        assertThat(testPerfiles.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testPerfiles.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingPerfiles() throws Exception {
        int databaseSizeBeforeUpdate = perfilesRepository.findAll().size();
        perfiles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfiles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfiles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerfiles() throws Exception {
        int databaseSizeBeforeUpdate = perfilesRepository.findAll().size();
        perfiles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfiles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerfiles() throws Exception {
        int databaseSizeBeforeUpdate = perfilesRepository.findAll().size();
        perfiles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfiles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePerfilesWithPatch() throws Exception {
        // Initialize the database
        perfilesRepository.saveAndFlush(perfiles);

        int databaseSizeBeforeUpdate = perfilesRepository.findAll().size();

        // Update the perfiles using partial update
        Perfiles partialUpdatedPerfiles = new Perfiles();
        partialUpdatedPerfiles.setId(perfiles.getId());

        partialUpdatedPerfiles.imagen(UPDATED_IMAGEN).nombre(UPDATED_NOMBRE);

        restPerfilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfiles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerfiles))
            )
            .andExpect(status().isOk());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeUpdate);
        Perfiles testPerfiles = perfilesList.get(perfilesList.size() - 1);
        assertThat(testPerfiles.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testPerfiles.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdatePerfilesWithPatch() throws Exception {
        // Initialize the database
        perfilesRepository.saveAndFlush(perfiles);

        int databaseSizeBeforeUpdate = perfilesRepository.findAll().size();

        // Update the perfiles using partial update
        Perfiles partialUpdatedPerfiles = new Perfiles();
        partialUpdatedPerfiles.setId(perfiles.getId());

        partialUpdatedPerfiles.imagen(UPDATED_IMAGEN).nombre(UPDATED_NOMBRE);

        restPerfilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfiles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerfiles))
            )
            .andExpect(status().isOk());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeUpdate);
        Perfiles testPerfiles = perfilesList.get(perfilesList.size() - 1);
        assertThat(testPerfiles.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testPerfiles.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingPerfiles() throws Exception {
        int databaseSizeBeforeUpdate = perfilesRepository.findAll().size();
        perfiles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, perfiles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfiles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerfiles() throws Exception {
        int databaseSizeBeforeUpdate = perfilesRepository.findAll().size();
        perfiles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfiles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerfiles() throws Exception {
        int databaseSizeBeforeUpdate = perfilesRepository.findAll().size();
        perfiles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(perfiles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Perfiles in the database
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerfiles() throws Exception {
        // Initialize the database
        perfilesRepository.saveAndFlush(perfiles);

        int databaseSizeBeforeDelete = perfilesRepository.findAll().size();

        // Delete the perfiles
        restPerfilesMockMvc
            .perform(delete(ENTITY_API_URL_ID, perfiles.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Perfiles> perfilesList = perfilesRepository.findAll();
        assertThat(perfilesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
