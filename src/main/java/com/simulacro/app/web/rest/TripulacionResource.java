package com.simulacro.app.web.rest;

import com.simulacro.app.repository.TripulacionRepository;
import com.simulacro.app.service.TripulacionQueryService;
import com.simulacro.app.service.TripulacionService;
import com.simulacro.app.service.criteria.TripulacionCriteria;
import com.simulacro.app.service.dto.TripulacionDTO;
import com.simulacro.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.simulacro.app.domain.Tripulacion}.
 */
@RestController
@RequestMapping("/api")
public class TripulacionResource {

    private final Logger log = LoggerFactory.getLogger(TripulacionResource.class);

    private static final String ENTITY_NAME = "tripulacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripulacionService tripulacionService;

    private final TripulacionRepository tripulacionRepository;

    private final TripulacionQueryService tripulacionQueryService;

    public TripulacionResource(
        TripulacionService tripulacionService,
        TripulacionRepository tripulacionRepository,
        TripulacionQueryService tripulacionQueryService
    ) {
        this.tripulacionService = tripulacionService;
        this.tripulacionRepository = tripulacionRepository;
        this.tripulacionQueryService = tripulacionQueryService;
    }

    /**
     * {@code POST  /tripulacions} : Create a new tripulacion.
     *
     * @param tripulacionDTO the tripulacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripulacionDTO, or with status {@code 400 (Bad Request)} if the tripulacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tripulacions")
    public ResponseEntity<TripulacionDTO> createTripulacion(@Valid @RequestBody TripulacionDTO tripulacionDTO) throws URISyntaxException {
        log.debug("REST request to save Tripulacion : {}", tripulacionDTO);
        if (tripulacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new tripulacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripulacionDTO result = tripulacionService.save(tripulacionDTO);
        return ResponseEntity
            .created(new URI("/api/tripulacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tripulacions/:id} : Updates an existing tripulacion.
     *
     * @param id the id of the tripulacionDTO to save.
     * @param tripulacionDTO the tripulacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripulacionDTO,
     * or with status {@code 400 (Bad Request)} if the tripulacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripulacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tripulacions/{id}")
    public ResponseEntity<TripulacionDTO> updateTripulacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TripulacionDTO tripulacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tripulacion : {}, {}", id, tripulacionDTO);
        if (tripulacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripulacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripulacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TripulacionDTO result = tripulacionService.save(tripulacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripulacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tripulacions/:id} : Partial updates given fields of an existing tripulacion, field will ignore if it is null
     *
     * @param id the id of the tripulacionDTO to save.
     * @param tripulacionDTO the tripulacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripulacionDTO,
     * or with status {@code 400 (Bad Request)} if the tripulacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tripulacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripulacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tripulacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripulacionDTO> partialUpdateTripulacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TripulacionDTO tripulacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tripulacion partially : {}, {}", id, tripulacionDTO);
        if (tripulacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripulacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripulacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripulacionDTO> result = tripulacionService.partialUpdate(tripulacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripulacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tripulacions} : get all the tripulacions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripulacions in body.
     */
    @GetMapping("/tripulacions")
    public ResponseEntity<List<TripulacionDTO>> getAllTripulacions(
        TripulacionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Tripulacions by criteria: {}", criteria);
        Page<TripulacionDTO> page = tripulacionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tripulacions/count} : count all the tripulacions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tripulacions/count")
    public ResponseEntity<Long> countTripulacions(TripulacionCriteria criteria) {
        log.debug("REST request to count Tripulacions by criteria: {}", criteria);
        return ResponseEntity.ok().body(tripulacionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tripulacions/:id} : get the "id" tripulacion.
     *
     * @param id the id of the tripulacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripulacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tripulacions/{id}")
    public ResponseEntity<TripulacionDTO> getTripulacion(@PathVariable Long id) {
        log.debug("REST request to get Tripulacion : {}", id);
        Optional<TripulacionDTO> tripulacionDTO = tripulacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripulacionDTO);
    }

    /**
     * {@code DELETE  /tripulacions/:id} : delete the "id" tripulacion.
     *
     * @param id the id of the tripulacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tripulacions/{id}")
    public ResponseEntity<Void> deleteTripulacion(@PathVariable Long id) {
        log.debug("REST request to delete Tripulacion : {}", id);
        tripulacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
