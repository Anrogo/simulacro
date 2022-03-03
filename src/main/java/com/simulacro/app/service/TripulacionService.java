package com.simulacro.app.service;

import com.simulacro.app.domain.Tripulacion;
import com.simulacro.app.repository.TripulacionRepository;
import com.simulacro.app.service.dto.TripulacionDTO;
import com.simulacro.app.service.mapper.TripulacionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tripulacion}.
 */
@Service
@Transactional
public class TripulacionService {

    private final Logger log = LoggerFactory.getLogger(TripulacionService.class);

    private final TripulacionRepository tripulacionRepository;

    private final TripulacionMapper tripulacionMapper;

    public TripulacionService(TripulacionRepository tripulacionRepository, TripulacionMapper tripulacionMapper) {
        this.tripulacionRepository = tripulacionRepository;
        this.tripulacionMapper = tripulacionMapper;
    }

    /**
     * Save a tripulacion.
     *
     * @param tripulacionDTO the entity to save.
     * @return the persisted entity.
     */
    public TripulacionDTO save(TripulacionDTO tripulacionDTO) {
        log.debug("Request to save Tripulacion : {}", tripulacionDTO);
        Tripulacion tripulacion = tripulacionMapper.toEntity(tripulacionDTO);
        tripulacion = tripulacionRepository.save(tripulacion);
        return tripulacionMapper.toDto(tripulacion);
    }

    /**
     * Partially update a tripulacion.
     *
     * @param tripulacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TripulacionDTO> partialUpdate(TripulacionDTO tripulacionDTO) {
        log.debug("Request to partially update Tripulacion : {}", tripulacionDTO);

        return tripulacionRepository
            .findById(tripulacionDTO.getId())
            .map(existingTripulacion -> {
                tripulacionMapper.partialUpdate(existingTripulacion, tripulacionDTO);

                return existingTripulacion;
            })
            .map(tripulacionRepository::save)
            .map(tripulacionMapper::toDto);
    }

    /**
     * Get all the tripulacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TripulacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tripulacions");
        return tripulacionRepository.findAll(pageable).map(tripulacionMapper::toDto);
    }

    /**
     * Get one tripulacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TripulacionDTO> findOne(Long id) {
        log.debug("Request to get Tripulacion : {}", id);
        return tripulacionRepository.findById(id).map(tripulacionMapper::toDto);
    }

    /**
     * Delete the tripulacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tripulacion : {}", id);
        tripulacionRepository.deleteById(id);
    }
}
