package com.simulacro.app.repository;

import com.simulacro.app.domain.Tripulacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tripulacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripulacionRepository extends JpaRepository<Tripulacion, Long>, JpaSpecificationExecutor<Tripulacion> {}
