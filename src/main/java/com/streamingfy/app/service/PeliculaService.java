package com.streamingfy.app.service;

import com.streamingfy.app.domain.Pelicula;
import com.streamingfy.app.repository.PeliculaRepository;
import com.streamingfy.app.repository.specifications.PeliculaSpecification;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pelicula}.
 */
@Service
@Transactional
public class PeliculaService {

    private final Logger log = LoggerFactory.getLogger(PeliculaService.class);

    private final PeliculaRepository peliculaRepository;

    public PeliculaService(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    /**
     * Save a pelicula.
     *
     * @param pelicula the entity to save.
     * @return the persisted entity.
     */
    public Pelicula save(Pelicula pelicula) {
        log.debug("Request to save Pelicula : {}", pelicula);
        return peliculaRepository.save(pelicula);
    }

    /**
     * Update a pelicula.
     *
     * @param pelicula the entity to save.
     * @return the persisted entity.
     */
    public Pelicula update(Pelicula pelicula) {
        log.debug("Request to save Pelicula : {}", pelicula);
        return peliculaRepository.save(pelicula);
    }

    /**
     * Partially update a pelicula.
     *
     * @param pelicula the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Pelicula> partialUpdate(Pelicula pelicula) {
        log.debug("Request to partially update Pelicula : {}", pelicula);

        return peliculaRepository
            .findById(pelicula.getId())
            .map(existingPelicula -> {
                if (pelicula.getTitulo() != null) {
                    existingPelicula.setTitulo(pelicula.getTitulo());
                }
                if (pelicula.getEstreno() != null) {
                    existingPelicula.setEstreno(pelicula.getEstreno());
                }
                if (pelicula.getDescripcion() != null) {
                    existingPelicula.setDescripcion(pelicula.getDescripcion());
                }
                if (pelicula.getGenero() != null) {
                    existingPelicula.setGenero(pelicula.getGenero());
                }
                if (pelicula.getCalificacion() != null) {
                    existingPelicula.setCalificacion(pelicula.getCalificacion());
                }
                if (pelicula.getImagen() != null) {
                    existingPelicula.setImagen(pelicula.getImagen());
                }
                if (pelicula.getUrl() != null) {
                    existingPelicula.setUrl(pelicula.getUrl());
                }
                if (pelicula.getDescripcion() != null) {
                    existingPelicula.setDescripcion(pelicula.getDescripcion());
                }

                return existingPelicula;
            })
            .map(peliculaRepository::save);
    }

    /**
     * Get all the peliculas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Pelicula> findAll(Pageable pageable) {
        log.debug("Request to get all Peliculas");
        return peliculaRepository.findAll(pageable);
    }

    /**
     * Get one pelicula by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Pelicula> findOne(Long id) {
        log.debug("Request to get Pelicula : {}", id);
        return peliculaRepository.findById(id);
    }

    /*    @Transactional(readOnly = true)
    public List<Pelicula> buscarfiltro(String filtro){
        log.debug("Llega a consulta filtro");
       /*  return peliculaRepository.findAll(PeliculaSpecification.busquedaPelicula(filtro)); 
       return peliculaRepository.findAll(PeliculaSpecification.busquedaPelicula(filtro));
        
} */

    public List<Pelicula> buscarfiltro(String filtro) {
        log.debug("Llega a consulta filtro");
        return peliculaRepository.findByTituloContaining(filtro);
    }

    /**
     * Delete the pelicula by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pelicula : {}", id);
        peliculaRepository.deleteById(id);
    }
}
