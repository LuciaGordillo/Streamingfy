package com.streamingfy.app.web.rest;

import static com.streamingfy.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.streamingfy.app.IntegrationTest;
import com.streamingfy.app.domain.Pelicula;
import com.streamingfy.app.repository.PeliculaRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link PeliculaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeliculaResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_GENERO = "AAAAAAAAAA";
    private static final String UPDATED_GENERO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CALIFICACION = 1;
    private static final Integer UPDATED_CALIFICACION = 2;

    private static final ZonedDateTime DEFAULT_ESTRENO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ESTRENO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEN = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/peliculas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeliculaMockMvc;

    private Pelicula pelicula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pelicula createEntity(EntityManager em) {
        Pelicula pelicula = new Pelicula()
            .titulo(DEFAULT_TITULO)
            .genero(DEFAULT_GENERO)
            .descripcion(DEFAULT_DESCRIPCION)
            .calificacion(DEFAULT_CALIFICACION)
            .estreno(DEFAULT_ESTRENO)
            .imagen(DEFAULT_IMAGEN)
            .url(DEFAULT_URL);
        return pelicula;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pelicula createUpdatedEntity(EntityManager em) {
        Pelicula pelicula = new Pelicula()
            .titulo(UPDATED_TITULO)
            .genero(UPDATED_GENERO)
            .descripcion(UPDATED_DESCRIPCION)
            .calificacion(UPDATED_CALIFICACION)
            .estreno(UPDATED_ESTRENO)
            .imagen(UPDATED_IMAGEN)
            .url(UPDATED_URL);
        return pelicula;
    }

    @BeforeEach
    public void initTest() {
        pelicula = createEntity(em);
    }

    @Test
    @Transactional
    void createPelicula() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();
        // Create the Pelicula
        restPeliculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isCreated());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate + 1);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testPelicula.getGenero()).isEqualTo(DEFAULT_GENERO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPelicula.getCalificacion()).isEqualTo(DEFAULT_CALIFICACION);
        assertThat(testPelicula.getEstreno()).isEqualTo(DEFAULT_ESTRENO);
        assertThat(testPelicula.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testPelicula.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createPeliculaWithExistingId() throws Exception {
        // Create the Pelicula with an existing ID
        pelicula.setId(1L);

        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeliculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPeliculas() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList
        restPeliculaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pelicula.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].calificacion").value(hasItem(DEFAULT_CALIFICACION)))
            .andExpect(jsonPath("$.[*].estreno").value(hasItem(sameInstant(DEFAULT_ESTRENO))))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getPelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get the pelicula
        restPeliculaMockMvc
            .perform(get(ENTITY_API_URL_ID, pelicula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pelicula.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.calificacion").value(DEFAULT_CALIFICACION))
            .andExpect(jsonPath("$.estreno").value(sameInstant(DEFAULT_ESTRENO)))
            .andExpect(jsonPath("$.imagen").value(DEFAULT_IMAGEN))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getNonExistingPelicula() throws Exception {
        // Get the pelicula
        restPeliculaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula
        Pelicula updatedPelicula = peliculaRepository.findById(pelicula.getId()).get();
        // Disconnect from session so that the updates on updatedPelicula are not directly saved in db
        em.detach(updatedPelicula);
        updatedPelicula
            .titulo(UPDATED_TITULO)
            .genero(UPDATED_GENERO)
            .descripcion(UPDATED_DESCRIPCION)
            .calificacion(UPDATED_CALIFICACION)
            .estreno(UPDATED_ESTRENO)
            .imagen(UPDATED_IMAGEN)
            .url(UPDATED_URL);

        restPeliculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPelicula.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPelicula))
            )
            .andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testPelicula.getGenero()).isEqualTo(UPDATED_GENERO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPelicula.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testPelicula.getEstreno()).isEqualTo(UPDATED_ESTRENO);
        assertThat(testPelicula.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testPelicula.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pelicula.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pelicula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pelicula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeliculaWithPatch() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula using partial update
        Pelicula partialUpdatedPelicula = new Pelicula();
        partialUpdatedPelicula.setId(pelicula.getId());

        partialUpdatedPelicula.calificacion(UPDATED_CALIFICACION);

        restPeliculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPelicula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPelicula))
            )
            .andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testPelicula.getGenero()).isEqualTo(DEFAULT_GENERO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPelicula.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testPelicula.getEstreno()).isEqualTo(DEFAULT_ESTRENO);
        assertThat(testPelicula.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testPelicula.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void fullUpdatePeliculaWithPatch() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula using partial update
        Pelicula partialUpdatedPelicula = new Pelicula();
        partialUpdatedPelicula.setId(pelicula.getId());

        partialUpdatedPelicula
            .titulo(UPDATED_TITULO)
            .genero(UPDATED_GENERO)
            .descripcion(UPDATED_DESCRIPCION)
            .calificacion(UPDATED_CALIFICACION)
            .estreno(UPDATED_ESTRENO)
            .imagen(UPDATED_IMAGEN)
            .url(UPDATED_URL);

        restPeliculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPelicula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPelicula))
            )
            .andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testPelicula.getGenero()).isEqualTo(UPDATED_GENERO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPelicula.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testPelicula.getEstreno()).isEqualTo(UPDATED_ESTRENO);
        assertThat(testPelicula.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testPelicula.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pelicula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pelicula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pelicula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeDelete = peliculaRepository.findAll().size();

        // Delete the pelicula
        restPeliculaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pelicula.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
