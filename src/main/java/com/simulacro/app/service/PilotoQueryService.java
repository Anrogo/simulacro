package com.simulacro.app.service;

import com.simulacro.app.domain.*; // for static metamodels
import com.simulacro.app.domain.Piloto;
import com.simulacro.app.repository.PilotoRepository;
import com.simulacro.app.service.criteria.PilotoCriteria;
import com.simulacro.app.service.dto.PilotoDTO;
import com.simulacro.app.service.mapper.PilotoMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Piloto} entities in the database.
 * The main input is a {@link PilotoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PilotoDTO} or a {@link Page} of {@link PilotoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PilotoQueryService extends QueryService<Piloto> {

    private final Logger log = LoggerFactory.getLogger(PilotoQueryService.class);

    private final PilotoRepository pilotoRepository;

    private final PilotoMapper pilotoMapper;

    public PilotoQueryService(PilotoRepository pilotoRepository, PilotoMapper pilotoMapper) {
        this.pilotoRepository = pilotoRepository;
        this.pilotoMapper = pilotoMapper;
    }

    /**
     * Return a {@link List} of {@link PilotoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PilotoDTO> findByCriteria(PilotoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Piloto> specification = createSpecification(criteria);
        return pilotoMapper.toDto(pilotoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PilotoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PilotoDTO> findByCriteria(PilotoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Piloto> specification = createSpecification(criteria);
        return pilotoRepository.findAll(specification, page).map(pilotoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PilotoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Piloto> specification = createSpecification(criteria);
        return pilotoRepository.count(specification);
    }

    /**
     * Function to convert {@link PilotoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Piloto> createSpecification(PilotoCriteria criteria) {
        Specification<Piloto> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Piloto_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Piloto_.nombre));
            }
            if (criteria.getDni() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDni(), Piloto_.dni));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), Piloto_.direccion));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Piloto_.email));
            }
            if (criteria.getHorasVuelo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHorasVuelo(), Piloto_.horasVuelo));
            }
        }
        return specification;
    }
}
