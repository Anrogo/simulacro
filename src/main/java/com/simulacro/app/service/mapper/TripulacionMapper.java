package com.simulacro.app.service.mapper;

import com.simulacro.app.domain.Tripulacion;
import com.simulacro.app.service.dto.TripulacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tripulacion} and its DTO {@link TripulacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TripulacionMapper extends EntityMapper<TripulacionDTO, Tripulacion> {}
