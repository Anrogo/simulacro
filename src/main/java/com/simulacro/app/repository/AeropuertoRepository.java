package com.simulacro.app.repository;

import com.simulacro.app.domain.Aeropuerto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Aeropuerto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AeropuertoRepository extends JpaRepository<Aeropuerto, Long>, JpaSpecificationExecutor<Aeropuerto> {}
