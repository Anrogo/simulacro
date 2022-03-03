package com.simulacro.app.repository;

import com.simulacro.app.domain.Vuelo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface VueloRepositoryWithBagRelationships {
    Optional<Vuelo> fetchBagRelationships(Optional<Vuelo> vuelo);

    List<Vuelo> fetchBagRelationships(List<Vuelo> vuelos);

    Page<Vuelo> fetchBagRelationships(Page<Vuelo> vuelos);
}
