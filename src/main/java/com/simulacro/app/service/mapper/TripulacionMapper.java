package com.simulacro.app.service.mapper;

import com.simulacro.app.domain.Tripulacion;
import com.simulacro.app.service.dto.TripulacionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tripulacion} and its DTO {@link TripulacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TripulacionMapper extends EntityMapper<TripulacionDTO, Tripulacion> {
    @Named("dniSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dni", source = "dni")
    Set<TripulacionDTO> toDtoDniSet(Set<Tripulacion> tripulacion);
}
