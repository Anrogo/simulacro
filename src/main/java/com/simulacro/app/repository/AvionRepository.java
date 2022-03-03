package com.simulacro.app.repository;

import com.simulacro.app.domain.Avion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Avion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvionRepository extends JpaRepository<Avion, Long>, JpaSpecificationExecutor<Avion> {}
