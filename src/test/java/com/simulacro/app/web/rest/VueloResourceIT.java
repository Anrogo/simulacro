package com.simulacro.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simulacro.app.IntegrationTest;
import com.simulacro.app.domain.Aeropuerto;
import com.simulacro.app.domain.Avion;
import com.simulacro.app.domain.Piloto;
import com.simulacro.app.domain.Tripulacion;
import com.simulacro.app.domain.Vuelo;
import com.simulacro.app.repository.VueloRepository;
import com.simulacro.app.service.VueloService;
import com.simulacro.app.service.criteria.VueloCriteria;
import com.simulacro.app.service.dto.VueloDTO;
import com.simulacro.app.service.mapper.VueloMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VueloResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VueloResourceIT {

    private static final String DEFAULT_NUM_VUELO = "EA-8464-5870-8941-1478";
    private static final String UPDATED_NUM_VUELO = "IK-5401";

    private static final Instant DEFAULT_HORA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/vuelos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VueloRepository vueloRepository;

    @Mock
    private VueloRepository vueloRepositoryMock;

    @Autowired
    private VueloMapper vueloMapper;

    @Mock
    private VueloService vueloServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVueloMockMvc;

    private Vuelo vuelo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vuelo createEntity(EntityManager em) {
        Vuelo vuelo = new Vuelo().numVuelo(DEFAULT_NUM_VUELO).hora(DEFAULT_HORA);
        return vuelo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vuelo createUpdatedEntity(EntityManager em) {
        Vuelo vuelo = new Vuelo().numVuelo(UPDATED_NUM_VUELO).hora(UPDATED_HORA);
        return vuelo;
    }

    @BeforeEach
    public void initTest() {
        vuelo = createEntity(em);
    }

    @Test
    @Transactional
    void createVuelo() throws Exception {
        int databaseSizeBeforeCreate = vueloRepository.findAll().size();
        // Create the Vuelo
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);
        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vueloDTO)))
            .andExpect(status().isCreated());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeCreate + 1);
        Vuelo testVuelo = vueloList.get(vueloList.size() - 1);
        assertThat(testVuelo.getNumVuelo()).isEqualTo(DEFAULT_NUM_VUELO);
        assertThat(testVuelo.getHora()).isEqualTo(DEFAULT_HORA);
    }

    @Test
    @Transactional
    void createVueloWithExistingId() throws Exception {
        // Create the Vuelo with an existing ID
        vuelo.setId(1L);
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);

        int databaseSizeBeforeCreate = vueloRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vueloDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumVueloIsRequired() throws Exception {
        int databaseSizeBeforeTest = vueloRepository.findAll().size();
        // set the field null
        vuelo.setNumVuelo(null);

        // Create the Vuelo, which fails.
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);

        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vueloDTO)))
            .andExpect(status().isBadRequest());

        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoraIsRequired() throws Exception {
        int databaseSizeBeforeTest = vueloRepository.findAll().size();
        // set the field null
        vuelo.setHora(null);

        // Create the Vuelo, which fails.
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);

        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vueloDTO)))
            .andExpect(status().isBadRequest());

        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVuelos() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList
        restVueloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vuelo.getId().intValue())))
            .andExpect(jsonPath("$.[*].numVuelo").value(hasItem(DEFAULT_NUM_VUELO)))
            .andExpect(jsonPath("$.[*].hora").value(hasItem(DEFAULT_HORA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVuelosWithEagerRelationshipsIsEnabled() throws Exception {
        when(vueloServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVueloMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vueloServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVuelosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vueloServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVueloMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vueloServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVuelo() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get the vuelo
        restVueloMockMvc
            .perform(get(ENTITY_API_URL_ID, vuelo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vuelo.getId().intValue()))
            .andExpect(jsonPath("$.numVuelo").value(DEFAULT_NUM_VUELO))
            .andExpect(jsonPath("$.hora").value(DEFAULT_HORA.toString()));
    }

    @Test
    @Transactional
    void getVuelosByIdFiltering() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        Long id = vuelo.getId();

        defaultVueloShouldBeFound("id.equals=" + id);
        defaultVueloShouldNotBeFound("id.notEquals=" + id);

        defaultVueloShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVueloShouldNotBeFound("id.greaterThan=" + id);

        defaultVueloShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVueloShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVuelosByNumVueloIsEqualToSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where numVuelo equals to DEFAULT_NUM_VUELO
        defaultVueloShouldBeFound("numVuelo.equals=" + DEFAULT_NUM_VUELO);

        // Get all the vueloList where numVuelo equals to UPDATED_NUM_VUELO
        defaultVueloShouldNotBeFound("numVuelo.equals=" + UPDATED_NUM_VUELO);
    }

    @Test
    @Transactional
    void getAllVuelosByNumVueloIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where numVuelo not equals to DEFAULT_NUM_VUELO
        defaultVueloShouldNotBeFound("numVuelo.notEquals=" + DEFAULT_NUM_VUELO);

        // Get all the vueloList where numVuelo not equals to UPDATED_NUM_VUELO
        defaultVueloShouldBeFound("numVuelo.notEquals=" + UPDATED_NUM_VUELO);
    }

    @Test
    @Transactional
    void getAllVuelosByNumVueloIsInShouldWork() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where numVuelo in DEFAULT_NUM_VUELO or UPDATED_NUM_VUELO
        defaultVueloShouldBeFound("numVuelo.in=" + DEFAULT_NUM_VUELO + "," + UPDATED_NUM_VUELO);

        // Get all the vueloList where numVuelo equals to UPDATED_NUM_VUELO
        defaultVueloShouldNotBeFound("numVuelo.in=" + UPDATED_NUM_VUELO);
    }

    @Test
    @Transactional
    void getAllVuelosByNumVueloIsNullOrNotNull() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where numVuelo is not null
        defaultVueloShouldBeFound("numVuelo.specified=true");

        // Get all the vueloList where numVuelo is null
        defaultVueloShouldNotBeFound("numVuelo.specified=false");
    }

    @Test
    @Transactional
    void getAllVuelosByNumVueloContainsSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where numVuelo contains DEFAULT_NUM_VUELO
        defaultVueloShouldBeFound("numVuelo.contains=" + DEFAULT_NUM_VUELO);

        // Get all the vueloList where numVuelo contains UPDATED_NUM_VUELO
        defaultVueloShouldNotBeFound("numVuelo.contains=" + UPDATED_NUM_VUELO);
    }

    @Test
    @Transactional
    void getAllVuelosByNumVueloNotContainsSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where numVuelo does not contain DEFAULT_NUM_VUELO
        defaultVueloShouldNotBeFound("numVuelo.doesNotContain=" + DEFAULT_NUM_VUELO);

        // Get all the vueloList where numVuelo does not contain UPDATED_NUM_VUELO
        defaultVueloShouldBeFound("numVuelo.doesNotContain=" + UPDATED_NUM_VUELO);
    }

    @Test
    @Transactional
    void getAllVuelosByHoraIsEqualToSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where hora equals to DEFAULT_HORA
        defaultVueloShouldBeFound("hora.equals=" + DEFAULT_HORA);

        // Get all the vueloList where hora equals to UPDATED_HORA
        defaultVueloShouldNotBeFound("hora.equals=" + UPDATED_HORA);
    }

    @Test
    @Transactional
    void getAllVuelosByHoraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where hora not equals to DEFAULT_HORA
        defaultVueloShouldNotBeFound("hora.notEquals=" + DEFAULT_HORA);

        // Get all the vueloList where hora not equals to UPDATED_HORA
        defaultVueloShouldBeFound("hora.notEquals=" + UPDATED_HORA);
    }

    @Test
    @Transactional
    void getAllVuelosByHoraIsInShouldWork() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where hora in DEFAULT_HORA or UPDATED_HORA
        defaultVueloShouldBeFound("hora.in=" + DEFAULT_HORA + "," + UPDATED_HORA);

        // Get all the vueloList where hora equals to UPDATED_HORA
        defaultVueloShouldNotBeFound("hora.in=" + UPDATED_HORA);
    }

    @Test
    @Transactional
    void getAllVuelosByHoraIsNullOrNotNull() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList where hora is not null
        defaultVueloShouldBeFound("hora.specified=true");

        // Get all the vueloList where hora is null
        defaultVueloShouldNotBeFound("hora.specified=false");
    }

    @Test
    @Transactional
    void getAllVuelosByOrigenIsEqualToSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);
        Aeropuerto origen;
        if (TestUtil.findAll(em, Aeropuerto.class).isEmpty()) {
            origen = AeropuertoResourceIT.createEntity(em);
            em.persist(origen);
            em.flush();
        } else {
            origen = TestUtil.findAll(em, Aeropuerto.class).get(0);
        }
        em.persist(origen);
        em.flush();
        vuelo.setOrigen(origen);
        vueloRepository.saveAndFlush(vuelo);
        Long origenId = origen.getId();

        // Get all the vueloList where origen equals to origenId
        defaultVueloShouldBeFound("origenId.equals=" + origenId);

        // Get all the vueloList where origen equals to (origenId + 1)
        defaultVueloShouldNotBeFound("origenId.equals=" + (origenId + 1));
    }

    @Test
    @Transactional
    void getAllVuelosByDestinoIsEqualToSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);
        Aeropuerto destino;
        if (TestUtil.findAll(em, Aeropuerto.class).isEmpty()) {
            destino = AeropuertoResourceIT.createEntity(em);
            em.persist(destino);
            em.flush();
        } else {
            destino = TestUtil.findAll(em, Aeropuerto.class).get(0);
        }
        em.persist(destino);
        em.flush();
        vuelo.setDestino(destino);
        vueloRepository.saveAndFlush(vuelo);
        Long destinoId = destino.getId();

        // Get all the vueloList where destino equals to destinoId
        defaultVueloShouldBeFound("destinoId.equals=" + destinoId);

        // Get all the vueloList where destino equals to (destinoId + 1)
        defaultVueloShouldNotBeFound("destinoId.equals=" + (destinoId + 1));
    }

    @Test
    @Transactional
    void getAllVuelosByAvionIsEqualToSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);
        Avion avion;
        if (TestUtil.findAll(em, Avion.class).isEmpty()) {
            avion = AvionResourceIT.createEntity(em);
            em.persist(avion);
            em.flush();
        } else {
            avion = TestUtil.findAll(em, Avion.class).get(0);
        }
        em.persist(avion);
        em.flush();
        vuelo.setAvion(avion);
        vueloRepository.saveAndFlush(vuelo);
        Long avionId = avion.getId();

        // Get all the vueloList where avion equals to avionId
        defaultVueloShouldBeFound("avionId.equals=" + avionId);

        // Get all the vueloList where avion equals to (avionId + 1)
        defaultVueloShouldNotBeFound("avionId.equals=" + (avionId + 1));
    }

    @Test
    @Transactional
    void getAllVuelosByPilotoIsEqualToSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);
        Piloto piloto;
        if (TestUtil.findAll(em, Piloto.class).isEmpty()) {
            piloto = PilotoResourceIT.createEntity(em);
            em.persist(piloto);
            em.flush();
        } else {
            piloto = TestUtil.findAll(em, Piloto.class).get(0);
        }
        em.persist(piloto);
        em.flush();
        vuelo.setPiloto(piloto);
        vueloRepository.saveAndFlush(vuelo);
        Long pilotoId = piloto.getId();

        // Get all the vueloList where piloto equals to pilotoId
        defaultVueloShouldBeFound("pilotoId.equals=" + pilotoId);

        // Get all the vueloList where piloto equals to (pilotoId + 1)
        defaultVueloShouldNotBeFound("pilotoId.equals=" + (pilotoId + 1));
    }

    @Test
    @Transactional
    void getAllVuelosByTripulanteIsEqualToSomething() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);
        Tripulacion tripulante;
        if (TestUtil.findAll(em, Tripulacion.class).isEmpty()) {
            tripulante = TripulacionResourceIT.createEntity(em);
            em.persist(tripulante);
            em.flush();
        } else {
            tripulante = TestUtil.findAll(em, Tripulacion.class).get(0);
        }
        em.persist(tripulante);
        em.flush();
        vuelo.addTripulante(tripulante);
        vueloRepository.saveAndFlush(vuelo);
        Long tripulanteId = tripulante.getId();

        // Get all the vueloList where tripulante equals to tripulanteId
        defaultVueloShouldBeFound("tripulanteId.equals=" + tripulanteId);

        // Get all the vueloList where tripulante equals to (tripulanteId + 1)
        defaultVueloShouldNotBeFound("tripulanteId.equals=" + (tripulanteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVueloShouldBeFound(String filter) throws Exception {
        restVueloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vuelo.getId().intValue())))
            .andExpect(jsonPath("$.[*].numVuelo").value(hasItem(DEFAULT_NUM_VUELO)))
            .andExpect(jsonPath("$.[*].hora").value(hasItem(DEFAULT_HORA.toString())));

        // Check, that the count call also returns 1
        restVueloMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVueloShouldNotBeFound(String filter) throws Exception {
        restVueloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVueloMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVuelo() throws Exception {
        // Get the vuelo
        restVueloMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVuelo() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        int databaseSizeBeforeUpdate = vueloRepository.findAll().size();

        // Update the vuelo
        Vuelo updatedVuelo = vueloRepository.findById(vuelo.getId()).get();
        // Disconnect from session so that the updates on updatedVuelo are not directly saved in db
        em.detach(updatedVuelo);
        updatedVuelo.numVuelo(UPDATED_NUM_VUELO).hora(UPDATED_HORA);
        VueloDTO vueloDTO = vueloMapper.toDto(updatedVuelo);

        restVueloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vueloDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vueloDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeUpdate);
        Vuelo testVuelo = vueloList.get(vueloList.size() - 1);
        assertThat(testVuelo.getNumVuelo()).isEqualTo(UPDATED_NUM_VUELO);
        assertThat(testVuelo.getHora()).isEqualTo(UPDATED_HORA);
    }

    @Test
    @Transactional
    void putNonExistingVuelo() throws Exception {
        int databaseSizeBeforeUpdate = vueloRepository.findAll().size();
        vuelo.setId(count.incrementAndGet());

        // Create the Vuelo
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vueloDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vueloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVuelo() throws Exception {
        int databaseSizeBeforeUpdate = vueloRepository.findAll().size();
        vuelo.setId(count.incrementAndGet());

        // Create the Vuelo
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vueloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVuelo() throws Exception {
        int databaseSizeBeforeUpdate = vueloRepository.findAll().size();
        vuelo.setId(count.incrementAndGet());

        // Create the Vuelo
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vueloDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVueloWithPatch() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        int databaseSizeBeforeUpdate = vueloRepository.findAll().size();

        // Update the vuelo using partial update
        Vuelo partialUpdatedVuelo = new Vuelo();
        partialUpdatedVuelo.setId(vuelo.getId());

        partialUpdatedVuelo.hora(UPDATED_HORA);

        restVueloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVuelo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVuelo))
            )
            .andExpect(status().isOk());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeUpdate);
        Vuelo testVuelo = vueloList.get(vueloList.size() - 1);
        assertThat(testVuelo.getNumVuelo()).isEqualTo(DEFAULT_NUM_VUELO);
        assertThat(testVuelo.getHora()).isEqualTo(UPDATED_HORA);
    }

    @Test
    @Transactional
    void fullUpdateVueloWithPatch() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        int databaseSizeBeforeUpdate = vueloRepository.findAll().size();

        // Update the vuelo using partial update
        Vuelo partialUpdatedVuelo = new Vuelo();
        partialUpdatedVuelo.setId(vuelo.getId());

        partialUpdatedVuelo.numVuelo(UPDATED_NUM_VUELO).hora(UPDATED_HORA);

        restVueloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVuelo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVuelo))
            )
            .andExpect(status().isOk());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeUpdate);
        Vuelo testVuelo = vueloList.get(vueloList.size() - 1);
        assertThat(testVuelo.getNumVuelo()).isEqualTo(UPDATED_NUM_VUELO);
        assertThat(testVuelo.getHora()).isEqualTo(UPDATED_HORA);
    }

    @Test
    @Transactional
    void patchNonExistingVuelo() throws Exception {
        int databaseSizeBeforeUpdate = vueloRepository.findAll().size();
        vuelo.setId(count.incrementAndGet());

        // Create the Vuelo
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vueloDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vueloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVuelo() throws Exception {
        int databaseSizeBeforeUpdate = vueloRepository.findAll().size();
        vuelo.setId(count.incrementAndGet());

        // Create the Vuelo
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vueloDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVuelo() throws Exception {
        int databaseSizeBeforeUpdate = vueloRepository.findAll().size();
        vuelo.setId(count.incrementAndGet());

        // Create the Vuelo
        VueloDTO vueloDTO = vueloMapper.toDto(vuelo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vueloDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vuelo in the database
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVuelo() throws Exception {
        // Initialize the database
        vueloRepository.saveAndFlush(vuelo);

        int databaseSizeBeforeDelete = vueloRepository.findAll().size();

        // Delete the vuelo
        restVueloMockMvc
            .perform(delete(ENTITY_API_URL_ID, vuelo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vuelo> vueloList = vueloRepository.findAll();
        assertThat(vueloList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
