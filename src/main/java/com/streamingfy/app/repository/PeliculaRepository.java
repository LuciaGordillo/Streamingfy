package com.streamingfy.app.repository;

import com.streamingfy.app.domain.Pelicula;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pelicula entity.
 */

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    List<Pelicula> findByTituloContaining(String filtro);
}
