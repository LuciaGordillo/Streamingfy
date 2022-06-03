package com.streamingfy.app.repository;

import com.streamingfy.app.domain.Perfiles;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Perfiles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerfilesRepository extends JpaRepository<Perfiles, Long> {
    @Query("select perfiles from Perfiles perfiles where perfiles.user.login = ?#{principal.username}")
    List<Perfiles> findByUserIsCurrentUser();
}
