package com.simulacro.app.service;

import com.simulacro.app.domain.*; // for static metamodels
import com.simulacro.app.domain.Tripulacion;
import com.simulacro.app.repository.TripulacionRepository;
import com.simulacro.app.service.criteria.TripulacionCriteria;
import com.simulacro.app.service.dto.TripulacionDTO;
import com.simulacro.app.service.mapper.TripulacionMapper;
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
 * Service for executing complex queries for {@link Tripulacion} entities in the database.
 * The main input is a {@link TripulacionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TripulacionDTO} or a {@link Page} of {@link TripulacionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TripulacionQueryService extends QueryService<Tripulacion> {

    private final Logger log = LoggerFactory.getLogger(TripulacionQueryService.class);

    private final TripulacionRepository tripulacionRepository;

    private final TripulacionMapper tripulacionMapper;

    public TripulacionQueryService(TripulacionRepository tripulacionRepository, TripulacionMapper tripulacionMapper) {
        this.tripulacionRepository = tripulacionRepository;
        this.tripulacionMapper = tripulacionMapper;
    }

    /**
     * Return a {@link List} of {@link TripulacionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TripulacionDTO> findByCriteria(TripulacionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tripulacion> specification = createSpecification(criteria);
        return tripulacionMapper.toDto(tripulacionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TripulacionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TripulacionDTO> findByCriteria(TripulacionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tripulacion> specification = createSpecification(criteria);
        return tripulacionRepository.findAll(specification, page).map(tripulacionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TripulacionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tripulacion> specification = createSpecification(criteria);
        return tripulacionRepository.count(specification);
    }

    /**
     * Function to convert {@link TripulacionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tripulacion> createSpecification(TripulacionCriteria criteria) {
        Specification<Tripulacion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tripulacion_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Tripulacion_.nombre));
            }
            if (criteria.getDni() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDni(), Tripulacion_.dni));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), Tripulacion_.direccion));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Tripulacion_.email));
            }
        }
        return specification;
    }
}
