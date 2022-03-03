package com.simulacro.app.service;

import com.simulacro.app.domain.*; // for static metamodels
import com.simulacro.app.domain.Vuelo;
import com.simulacro.app.repository.VueloRepository;
import com.simulacro.app.service.criteria.VueloCriteria;
import com.simulacro.app.service.dto.VueloDTO;
import com.simulacro.app.service.mapper.VueloMapper;
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
 * Service for executing complex queries for {@link Vuelo} entities in the database.
 * The main input is a {@link VueloCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VueloDTO} or a {@link Page} of {@link VueloDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VueloQueryService extends QueryService<Vuelo> {

    private final Logger log = LoggerFactory.getLogger(VueloQueryService.class);

    private final VueloRepository vueloRepository;

    private final VueloMapper vueloMapper;

    public VueloQueryService(VueloRepository vueloRepository, VueloMapper vueloMapper) {
        this.vueloRepository = vueloRepository;
        this.vueloMapper = vueloMapper;
    }

    /**
     * Return a {@link List} of {@link VueloDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VueloDTO> findByCriteria(VueloCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vuelo> specification = createSpecification(criteria);
        return vueloMapper.toDto(vueloRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VueloDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VueloDTO> findByCriteria(VueloCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vuelo> specification = createSpecification(criteria);
        return vueloRepository.findAll(specification, page).map(vueloMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VueloCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vuelo> specification = createSpecification(criteria);
        return vueloRepository.count(specification);
    }

    /**
     * Function to convert {@link VueloCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vuelo> createSpecification(VueloCriteria criteria) {
        Specification<Vuelo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vuelo_.id));
            }
            if (criteria.getNumVuelo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumVuelo(), Vuelo_.numVuelo));
            }
            if (criteria.getHora() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHora(), Vuelo_.hora));
            }
            if (criteria.getOrigenId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrigenId(), root -> root.join(Vuelo_.origen, JoinType.LEFT).get(Aeropuerto_.id))
                    );
            }
            if (criteria.getDestinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDestinoId(), root -> root.join(Vuelo_.destino, JoinType.LEFT).get(Aeropuerto_.id))
                    );
            }
            if (criteria.getAvionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAvionId(), root -> root.join(Vuelo_.avion, JoinType.LEFT).get(Avion_.id))
                    );
            }
            if (criteria.getPilotoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPilotoId(), root -> root.join(Vuelo_.piloto, JoinType.LEFT).get(Piloto_.id))
                    );
            }
            if (criteria.getTripulanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTripulanteId(),
                            root -> root.join(Vuelo_.tripulantes, JoinType.LEFT).get(Tripulacion_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
