package com.streamingfy.app.web.rest;

import com.streamingfy.app.domain.Perfiles;
import com.streamingfy.app.repository.PerfilesRepository;
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
 * REST controller for managing {@link com.streamingfy.app.domain.Perfiles}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PerfilesResource {

    private final Logger log = LoggerFactory.getLogger(PerfilesResource.class);

    private static final String ENTITY_NAME = "perfiles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerfilesRepository perfilesRepository;

    public PerfilesResource(PerfilesRepository perfilesRepository) {
        this.perfilesRepository = perfilesRepository;
    }

    /**
     * {@code POST  /perfiles} : Create a new perfiles.
     *
     * @param perfiles the perfiles to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new perfiles, or with status {@code 400 (Bad Request)} if the perfiles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/perfiles")
    public ResponseEntity<Perfiles> createPerfiles(@RequestBody Perfiles perfiles) throws URISyntaxException {
        log.debug("REST request to save Perfiles : {}", perfiles);
        if (perfiles.getId() != null) {
            throw new BadRequestAlertException("A new perfiles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Perfiles result = perfilesRepository.save(perfiles);
        return ResponseEntity
            .created(new URI("/api/perfiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /perfiles/:id} : Updates an existing perfiles.
     *
     * @param id the id of the perfiles to save.
     * @param perfiles the perfiles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfiles,
     * or with status {@code 400 (Bad Request)} if the perfiles is not valid,
     * or with status {@code 500 (Internal Server Error)} if the perfiles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/perfiles/{id}")
    public ResponseEntity<Perfiles> updatePerfiles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Perfiles perfiles
    ) throws URISyntaxException {
        log.debug("REST request to update Perfiles : {}, {}", id, perfiles);
        if (perfiles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfiles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Perfiles result = perfilesRepository.save(perfiles);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perfiles.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /perfiles/:id} : Partial updates given fields of an existing perfiles, field will ignore if it is null
     *
     * @param id the id of the perfiles to save.
     * @param perfiles the perfiles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfiles,
     * or with status {@code 400 (Bad Request)} if the perfiles is not valid,
     * or with status {@code 404 (Not Found)} if the perfiles is not found,
     * or with status {@code 500 (Internal Server Error)} if the perfiles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/perfiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Perfiles> partialUpdatePerfiles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Perfiles perfiles
    ) throws URISyntaxException {
        log.debug("REST request to partial update Perfiles partially : {}, {}", id, perfiles);
        if (perfiles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfiles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Perfiles> result = perfilesRepository
            .findById(perfiles.getId())
            .map(existingPerfiles -> {
                if (perfiles.getImagen() != null) {
                    existingPerfiles.setImagen(perfiles.getImagen());
                }
                if (perfiles.getNombre() != null) {
                    existingPerfiles.setNombre(perfiles.getNombre());
                }

                return existingPerfiles;
            })
            .map(perfilesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perfiles.getId().toString())
        );
    }

    /**
     * {@code GET  /perfiles} : get all the perfiles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of perfiles in body.
     */
    @GetMapping("/perfiles")
    public List<Perfiles> getAllPerfiles() {
        log.debug("REST request to get all Perfiles");
        return perfilesRepository.findAll();
    }

    /**
     * {@code GET  /perfiles/:id} : get the "id" perfiles.
     *
     * @param id the id of the perfiles to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perfiles, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/perfiles/{id}")
    public ResponseEntity<Perfiles> getPerfiles(@PathVariable Long id) {
        log.debug("REST request to get Perfiles : {}", id);
        Optional<Perfiles> perfiles = perfilesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(perfiles);
    }

    /**
     * {@code DELETE  /perfiles/:id} : delete the "id" perfiles.
     *
     * @param id the id of the perfiles to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/perfiles/{id}")
    public ResponseEntity<Void> deletePerfiles(@PathVariable Long id) {
        log.debug("REST request to delete Perfiles : {}", id);
        perfilesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
