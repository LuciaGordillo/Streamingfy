package com.streamingfy.app.web.rest;

import com.streamingfy.app.domain.Pelicula;
import com.streamingfy.app.repository.PeliculaRepository;
import com.streamingfy.app.service.PeliculaService;
import com.streamingfy.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.streamingfy.app.domain.Pelicula}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PeliculaResource {

    private final Logger log = LoggerFactory.getLogger(PeliculaResource.class);

    private static final String ENTITY_NAME = "pelicula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeliculaRepository peliculaRepository;
    private final PeliculaService peliculaService;

    public PeliculaResource(PeliculaService peliculaService, PeliculaRepository peliculaRepository) {
        this.peliculaService = peliculaService;
        this.peliculaRepository = peliculaRepository;
    }

    /**
     * {@code POST  /peliculas} : Create a new pelicula.
     *
     * @param pelicula the pelicula to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pelicula, or with status {@code 400 (Bad Request)} if the pelicula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/peliculas")
    public ResponseEntity<Pelicula> createPelicula(@RequestBody Pelicula pelicula) throws URISyntaxException {
        log.debug("REST request to save Pelicula : {}", pelicula);
        if (pelicula.getId() != null) {
            throw new BadRequestAlertException("A new pelicula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pelicula result = peliculaRepository.save(pelicula);
        return ResponseEntity
            .created(new URI("/api/peliculas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /peliculas/:id} : Updates an existing pelicula.
     *
     * @param id the id of the pelicula to save.
     * @param pelicula the pelicula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pelicula,
     * or with status {@code 400 (Bad Request)} if the pelicula is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pelicula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> updatePelicula(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pelicula pelicula
    ) throws URISyntaxException {
        log.debug("REST request to update Pelicula : {}, {}", id, pelicula);
        if (pelicula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pelicula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peliculaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pelicula result = peliculaRepository.save(pelicula);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pelicula.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /peliculas/:id} : Partial updates given fields of an existing pelicula, field will ignore if it is null
     *
     * @param id the id of the pelicula to save.
     * @param pelicula the pelicula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pelicula,
     * or with status {@code 400 (Bad Request)} if the pelicula is not valid,
     * or with status {@code 404 (Not Found)} if the pelicula is not found,
     * or with status {@code 500 (Internal Server Error)} if the pelicula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/peliculas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pelicula> partialUpdatePelicula(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pelicula pelicula
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pelicula partially : {}, {}", id, pelicula);
        if (pelicula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pelicula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peliculaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pelicula> result = peliculaRepository
            .findById(pelicula.getId())
            .map(existingPelicula -> {
                if (pelicula.getTitulo() != null) {
                    existingPelicula.setTitulo(pelicula.getTitulo());
                }
                if (pelicula.getGenero() != null) {
                    existingPelicula.setGenero(pelicula.getGenero());
                }
                if (pelicula.getDescripcion() != null) {
                    existingPelicula.setDescripcion(pelicula.getDescripcion());
                }
                if (pelicula.getCalificacion() != null) {
                    existingPelicula.setCalificacion(pelicula.getCalificacion());
                }
                if (pelicula.getEstreno() != null) {
                    existingPelicula.setEstreno(pelicula.getEstreno());
                }
                if (pelicula.getImagen() != null) {
                    existingPelicula.setImagen(pelicula.getImagen());
                }
                if (pelicula.getUrl() != null) {
                    existingPelicula.setUrl(pelicula.getUrl());
                }

                return existingPelicula;
            })
            .map(peliculaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pelicula.getId().toString())
        );
    }

    /**
     * {@code GET  /peliculas} : get all the peliculas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peliculas in body.
     */
    @GetMapping("/peliculas")
    public List<Pelicula> getAllPeliculas() {
        log.debug("REST request to get all Peliculas");
        return peliculaRepository.findAll();
    }

    /**
     * {@code GET  /peliculas/:id} : get the "id" pelicula.
     *
     * @param id the id of the pelicula to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pelicula, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> getPelicula(@PathVariable Long id) {
        log.debug("REST request to get Pelicula : {}", id);
        Optional<Pelicula> pelicula = peliculaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pelicula);
    }

    @GetMapping("/peliculas/titulo/{filtro}")
    public List<Pelicula> getPeliculasFiltradas(@PathVariable String filtro) {
        return peliculaRepository.findByTituloContaining(filtro);
    }

    /**
     * {@code DELETE  /peliculas/:id} : delete the "id" pelicula.
     *
     * @param id the id of the pelicula to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/peliculas/{id}")
    public ResponseEntity<Void> deletePelicula(@PathVariable Long id) {
        log.debug("REST request to delete Pelicula : {}", id);
        peliculaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
