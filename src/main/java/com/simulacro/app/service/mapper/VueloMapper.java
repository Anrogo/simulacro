package com.simulacro.app.service.mapper;

import com.simulacro.app.domain.Vuelo;
import com.simulacro.app.service.dto.VueloDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vuelo} and its DTO {@link VueloDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VueloMapper extends EntityMapper<VueloDTO, Vuelo> {}
