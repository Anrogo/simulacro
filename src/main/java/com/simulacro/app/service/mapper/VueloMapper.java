package com.simulacro.app.service.mapper;

import com.simulacro.app.domain.Vuelo;
import com.simulacro.app.service.dto.VueloDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vuelo} and its DTO {@link VueloDTO}.
 */
@Mapper(componentModel = "spring", uses = { AeropuertoMapper.class, AvionMapper.class, PilotoMapper.class, TripulacionMapper.class })
public interface VueloMapper extends EntityMapper<VueloDTO, Vuelo> {
    @Mapping(target = "origen", source = "origen", qualifiedByName = "nombre")
    @Mapping(target = "destino", source = "destino", qualifiedByName = "nombre")
    @Mapping(target = "avion", source = "avion", qualifiedByName = "matricula")
    @Mapping(target = "piloto", source = "piloto", qualifiedByName = "dni")
    @Mapping(target = "tripulantes", source = "tripulantes", qualifiedByName = "dniSet")
    VueloDTO toDto(Vuelo s);

    @Mapping(target = "removeTripulante", ignore = true)
    Vuelo toEntity(VueloDTO vueloDTO);
}
