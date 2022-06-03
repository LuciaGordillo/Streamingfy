package com.streamingfy.app.web.rest;

import com.streamingfy.app.domain.Lista;
import com.streamingfy.app.repository.ListaRepository;
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
 * REST controller for managing {@link com.streamingfy.app.domain.Lista}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ListaResource {

    private final Logger log = LoggerFactory.getLogger(ListaResource.class);

    private static final String ENTITY_NAME = "lista";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ListaRepository listaRepository;

    public ListaResource(ListaRepository listaRepository) {
        this.listaRepository = listaRepository;
    }

    /**
     * {@code POST  /listas} : Create a new lista.
     *
     * @param lista the lista to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lista, or with status {@code 400 (Bad Request)} if the lista has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/listas")
    public ResponseEntity<Lista> createLista(@RequestBody Lista lista) throws URISyntaxException {
        log.debug("REST request to save Lista : {}", lista);
        if (lista.getId() != null) {
            throw new BadRequestAlertException("A new lista cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lista result = listaRepository.save(lista);
        return ResponseEntity
            .created(new URI("/api/listas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /listas/:id} : Updates an existing lista.
     *
     * @param id the id of the lista to save.
     * @param lista the lista to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lista,
     * or with status {@code 400 (Bad Request)} if the lista is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lista couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/listas/{id}")
    public ResponseEntity<Lista> updateLista(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lista lista)
        throws URISyntaxException {
        log.debug("REST request to update Lista : {}, {}", id, lista);
        if (lista.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lista.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!listaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Lista result = listaRepository.save(lista);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lista.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /listas/:id} : Partial updates given fields of an existing lista, field will ignore if it is null
     *
     * @param id the id of the lista to save.
     * @param lista the lista to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lista,
     * or with status {@code 400 (Bad Request)} if the lista is not valid,
     * or with status {@code 404 (Not Found)} if the lista is not found,
     * or with status {@code 500 (Internal Server Error)} if the lista couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/listas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Lista> partialUpdateLista(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lista lista)
        throws URISyntaxException {
        log.debug("REST request to partial update Lista partially : {}, {}", id, lista);
        if (lista.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lista.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!listaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lista> result = listaRepository
            .findById(lista.getId())
            .map(existingLista -> {
                if (lista.getCountryName() != null) {
                    existingLista.setCountryName(lista.getCountryName());
                }

                return existingLista;
            })
            .map(listaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lista.getId().toString())
        );
    }

    /**
     * {@code GET  /listas} : get all the listas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of listas in body.
     */
    @GetMapping("/listas")
    public List<Lista> getAllListas() {
        log.debug("REST request to get all Listas");
        return listaRepository.findAll();
    }

    /**
     * {@code GET  /listas/:id} : get the "id" lista.
     *
     * @param id the id of the lista to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lista, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/listas/{id}")
    public ResponseEntity<Lista> getLista(@PathVariable Long id) {
        log.debug("REST request to get Lista : {}", id);
        Optional<Lista> lista = listaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lista);
    }

    /**
     * {@code DELETE  /listas/:id} : delete the "id" lista.
     *
     * @param id the id of the lista to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/listas/{id}")
    public ResponseEntity<Void> deleteLista(@PathVariable Long id) {
        log.debug("REST request to delete Lista : {}", id);
        listaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
