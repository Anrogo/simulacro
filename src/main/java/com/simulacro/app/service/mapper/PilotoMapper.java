package com.simulacro.app.service.mapper;

import com.simulacro.app.domain.Piloto;
import com.simulacro.app.service.dto.PilotoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Piloto} and its DTO {@link PilotoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PilotoMapper extends EntityMapper<PilotoDTO, Piloto> {
    @Named("dni")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dni", source = "dni")
    PilotoDTO toDtoDni(Piloto piloto);
}
