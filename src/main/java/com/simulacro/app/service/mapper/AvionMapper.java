package com.simulacro.app.service.mapper;

import com.simulacro.app.domain.Avion;
import com.simulacro.app.service.dto.AvionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Avion} and its DTO {@link AvionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AvionMapper extends EntityMapper<AvionDTO, Avion> {
    @Named("matricula")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "matricula", source = "matricula")
    AvionDTO toDtoMatricula(Avion avion);
}
