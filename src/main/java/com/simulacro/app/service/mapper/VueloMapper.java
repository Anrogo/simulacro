package com.simulacro.app.service.mapper;

import com.simulacro.app.domain.Vuelo;
import com.simulacro.app.service.dto.VueloDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vuelo} and its DTO {@link VueloDTO}.
 */
@Mapper(componentModel = "spring", uses = { AeropuertoMapper.class })
public interface VueloMapper extends EntityMapper<VueloDTO, Vuelo> {
    @Mapping(target = "origen", source = "origen", qualifiedByName = "nombre")
    @Mapping(target = "destino", source = "destino", qualifiedByName = "nombre")
    VueloDTO toDto(Vuelo s);
}
