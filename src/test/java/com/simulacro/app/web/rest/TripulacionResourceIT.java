package com.simulacro.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simulacro.app.IntegrationTest;
import com.simulacro.app.domain.Tripulacion;
import com.simulacro.app.repository.TripulacionRepository;
import com.simulacro.app.service.criteria.TripulacionCriteria;
import com.simulacro.app.service.dto.TripulacionDTO;
import com.simulacro.app.service.mapper.TripulacionMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TripulacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripulacionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "62368361V";
    private static final String UPDATED_DNI = "08182201E";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "+iu@]y7..<";
    private static final String UPDATED_EMAIL = "m&0c@fz=+:z.Jd";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/tripulacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripulacionRepository tripulacionRepository;

    @Autowired
    private TripulacionMapper tripulacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripulacionMockMvc;

    private Tripulacion tripulacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tripulacion createEntity(EntityManager em) {
        Tripulacion tripulacion = new Tripulacion()
            .nombre(DEFAULT_NOMBRE)
            .dni(DEFAULT_DNI)
            .direccion(DEFAULT_DIRECCION)
            .email(DEFAULT_EMAIL)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
        return tripulacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tripulacion createUpdatedEntity(EntityManager em) {
        Tripulacion tripulacion = new Tripulacion()
            .nombre(UPDATED_NOMBRE)
            .dni(UPDATED_DNI)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        return tripulacion;
    }

    @BeforeEach
    public void initTest() {
        tripulacion = createEntity(em);
    }

    @Test
    @Transactional
    void createTripulacion() throws Exception {
        int databaseSizeBeforeCreate = tripulacionRepository.findAll().size();
        // Create the Tripulacion
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);
        restTripulacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeCreate + 1);
        Tripulacion testTripulacion = tripulacionList.get(tripulacionList.size() - 1);
        assertThat(testTripulacion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTripulacion.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testTripulacion.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testTripulacion.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTripulacion.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testTripulacion.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createTripulacionWithExistingId() throws Exception {
        // Create the Tripulacion with an existing ID
        tripulacion.setId(1L);
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        int databaseSizeBeforeCreate = tripulacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripulacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripulacionRepository.findAll().size();
        // set the field null
        tripulacion.setNombre(null);

        // Create the Tripulacion, which fails.
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        restTripulacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripulacionRepository.findAll().size();
        // set the field null
        tripulacion.setDni(null);

        // Create the Tripulacion, which fails.
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        restTripulacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripulacionRepository.findAll().size();
        // set the field null
        tripulacion.setEmail(null);

        // Create the Tripulacion, which fails.
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        restTripulacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTripulacions() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList
        restTripulacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripulacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))));
    }

    @Test
    @Transactional
    void getTripulacion() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get the tripulacion
        restTripulacionMockMvc
            .perform(get(ENTITY_API_URL_ID, tripulacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripulacion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    void getTripulacionsByIdFiltering() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        Long id = tripulacion.getId();

        defaultTripulacionShouldBeFound("id.equals=" + id);
        defaultTripulacionShouldNotBeFound("id.notEquals=" + id);

        defaultTripulacionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTripulacionShouldNotBeFound("id.greaterThan=" + id);

        defaultTripulacionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTripulacionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTripulacionsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where nombre equals to DEFAULT_NOMBRE
        defaultTripulacionShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the tripulacionList where nombre equals to UPDATED_NOMBRE
        defaultTripulacionShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTripulacionsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where nombre not equals to DEFAULT_NOMBRE
        defaultTripulacionShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the tripulacionList where nombre not equals to UPDATED_NOMBRE
        defaultTripulacionShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTripulacionsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultTripulacionShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the tripulacionList where nombre equals to UPDATED_NOMBRE
        defaultTripulacionShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTripulacionsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where nombre is not null
        defaultTripulacionShouldBeFound("nombre.specified=true");

        // Get all the tripulacionList where nombre is null
        defaultTripulacionShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTripulacionsByNombreContainsSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where nombre contains DEFAULT_NOMBRE
        defaultTripulacionShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the tripulacionList where nombre contains UPDATED_NOMBRE
        defaultTripulacionShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTripulacionsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where nombre does not contain DEFAULT_NOMBRE
        defaultTripulacionShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the tripulacionList where nombre does not contain UPDATED_NOMBRE
        defaultTripulacionShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where dni equals to DEFAULT_DNI
        defaultTripulacionShouldBeFound("dni.equals=" + DEFAULT_DNI);

        // Get all the tripulacionList where dni equals to UPDATED_DNI
        defaultTripulacionShouldNotBeFound("dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDniIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where dni not equals to DEFAULT_DNI
        defaultTripulacionShouldNotBeFound("dni.notEquals=" + DEFAULT_DNI);

        // Get all the tripulacionList where dni not equals to UPDATED_DNI
        defaultTripulacionShouldBeFound("dni.notEquals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDniIsInShouldWork() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where dni in DEFAULT_DNI or UPDATED_DNI
        defaultTripulacionShouldBeFound("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI);

        // Get all the tripulacionList where dni equals to UPDATED_DNI
        defaultTripulacionShouldNotBeFound("dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where dni is not null
        defaultTripulacionShouldBeFound("dni.specified=true");

        // Get all the tripulacionList where dni is null
        defaultTripulacionShouldNotBeFound("dni.specified=false");
    }

    @Test
    @Transactional
    void getAllTripulacionsByDniContainsSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where dni contains DEFAULT_DNI
        defaultTripulacionShouldBeFound("dni.contains=" + DEFAULT_DNI);

        // Get all the tripulacionList where dni contains UPDATED_DNI
        defaultTripulacionShouldNotBeFound("dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDniNotContainsSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where dni does not contain DEFAULT_DNI
        defaultTripulacionShouldNotBeFound("dni.doesNotContain=" + DEFAULT_DNI);

        // Get all the tripulacionList where dni does not contain UPDATED_DNI
        defaultTripulacionShouldBeFound("dni.doesNotContain=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where direccion equals to DEFAULT_DIRECCION
        defaultTripulacionShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the tripulacionList where direccion equals to UPDATED_DIRECCION
        defaultTripulacionShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where direccion not equals to DEFAULT_DIRECCION
        defaultTripulacionShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the tripulacionList where direccion not equals to UPDATED_DIRECCION
        defaultTripulacionShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultTripulacionShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the tripulacionList where direccion equals to UPDATED_DIRECCION
        defaultTripulacionShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where direccion is not null
        defaultTripulacionShouldBeFound("direccion.specified=true");

        // Get all the tripulacionList where direccion is null
        defaultTripulacionShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllTripulacionsByDireccionContainsSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where direccion contains DEFAULT_DIRECCION
        defaultTripulacionShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the tripulacionList where direccion contains UPDATED_DIRECCION
        defaultTripulacionShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllTripulacionsByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where direccion does not contain DEFAULT_DIRECCION
        defaultTripulacionShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the tripulacionList where direccion does not contain UPDATED_DIRECCION
        defaultTripulacionShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllTripulacionsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where email equals to DEFAULT_EMAIL
        defaultTripulacionShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the tripulacionList where email equals to UPDATED_EMAIL
        defaultTripulacionShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTripulacionsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where email not equals to DEFAULT_EMAIL
        defaultTripulacionShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the tripulacionList where email not equals to UPDATED_EMAIL
        defaultTripulacionShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTripulacionsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTripulacionShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the tripulacionList where email equals to UPDATED_EMAIL
        defaultTripulacionShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTripulacionsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where email is not null
        defaultTripulacionShouldBeFound("email.specified=true");

        // Get all the tripulacionList where email is null
        defaultTripulacionShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllTripulacionsByEmailContainsSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where email contains DEFAULT_EMAIL
        defaultTripulacionShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the tripulacionList where email contains UPDATED_EMAIL
        defaultTripulacionShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTripulacionsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        // Get all the tripulacionList where email does not contain DEFAULT_EMAIL
        defaultTripulacionShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the tripulacionList where email does not contain UPDATED_EMAIL
        defaultTripulacionShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTripulacionShouldBeFound(String filter) throws Exception {
        restTripulacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripulacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))));

        // Check, that the count call also returns 1
        restTripulacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTripulacionShouldNotBeFound(String filter) throws Exception {
        restTripulacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTripulacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTripulacion() throws Exception {
        // Get the tripulacion
        restTripulacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTripulacion() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        int databaseSizeBeforeUpdate = tripulacionRepository.findAll().size();

        // Update the tripulacion
        Tripulacion updatedTripulacion = tripulacionRepository.findById(tripulacion.getId()).get();
        // Disconnect from session so that the updates on updatedTripulacion are not directly saved in db
        em.detach(updatedTripulacion);
        updatedTripulacion
            .nombre(UPDATED_NOMBRE)
            .dni(UPDATED_DNI)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(updatedTripulacion);

        restTripulacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripulacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeUpdate);
        Tripulacion testTripulacion = tripulacionList.get(tripulacionList.size() - 1);
        assertThat(testTripulacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTripulacion.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testTripulacion.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testTripulacion.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTripulacion.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testTripulacion.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingTripulacion() throws Exception {
        int databaseSizeBeforeUpdate = tripulacionRepository.findAll().size();
        tripulacion.setId(count.incrementAndGet());

        // Create the Tripulacion
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripulacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripulacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripulacion() throws Exception {
        int databaseSizeBeforeUpdate = tripulacionRepository.findAll().size();
        tripulacion.setId(count.incrementAndGet());

        // Create the Tripulacion
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripulacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripulacion() throws Exception {
        int databaseSizeBeforeUpdate = tripulacionRepository.findAll().size();
        tripulacion.setId(count.incrementAndGet());

        // Create the Tripulacion
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripulacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripulacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripulacionWithPatch() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        int databaseSizeBeforeUpdate = tripulacionRepository.findAll().size();

        // Update the tripulacion using partial update
        Tripulacion partialUpdatedTripulacion = new Tripulacion();
        partialUpdatedTripulacion.setId(tripulacion.getId());

        partialUpdatedTripulacion
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restTripulacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripulacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripulacion))
            )
            .andExpect(status().isOk());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeUpdate);
        Tripulacion testTripulacion = tripulacionList.get(tripulacionList.size() - 1);
        assertThat(testTripulacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTripulacion.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testTripulacion.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testTripulacion.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTripulacion.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testTripulacion.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTripulacionWithPatch() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        int databaseSizeBeforeUpdate = tripulacionRepository.findAll().size();

        // Update the tripulacion using partial update
        Tripulacion partialUpdatedTripulacion = new Tripulacion();
        partialUpdatedTripulacion.setId(tripulacion.getId());

        partialUpdatedTripulacion
            .nombre(UPDATED_NOMBRE)
            .dni(UPDATED_DNI)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restTripulacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripulacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripulacion))
            )
            .andExpect(status().isOk());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeUpdate);
        Tripulacion testTripulacion = tripulacionList.get(tripulacionList.size() - 1);
        assertThat(testTripulacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTripulacion.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testTripulacion.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testTripulacion.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTripulacion.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testTripulacion.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTripulacion() throws Exception {
        int databaseSizeBeforeUpdate = tripulacionRepository.findAll().size();
        tripulacion.setId(count.incrementAndGet());

        // Create the Tripulacion
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripulacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripulacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripulacion() throws Exception {
        int databaseSizeBeforeUpdate = tripulacionRepository.findAll().size();
        tripulacion.setId(count.incrementAndGet());

        // Create the Tripulacion
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripulacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripulacion() throws Exception {
        int databaseSizeBeforeUpdate = tripulacionRepository.findAll().size();
        tripulacion.setId(count.incrementAndGet());

        // Create the Tripulacion
        TripulacionDTO tripulacionDTO = tripulacionMapper.toDto(tripulacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripulacionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tripulacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tripulacion in the database
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTripulacion() throws Exception {
        // Initialize the database
        tripulacionRepository.saveAndFlush(tripulacion);

        int databaseSizeBeforeDelete = tripulacionRepository.findAll().size();

        // Delete the tripulacion
        restTripulacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripulacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tripulacion> tripulacionList = tripulacionRepository.findAll();
        assertThat(tripulacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
