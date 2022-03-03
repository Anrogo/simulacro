package com.simulacro.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simulacro.app.IntegrationTest;
import com.simulacro.app.domain.Piloto;
import com.simulacro.app.repository.PilotoRepository;
import com.simulacro.app.service.criteria.PilotoCriteria;
import com.simulacro.app.service.dto.PilotoDTO;
import com.simulacro.app.service.mapper.PilotoMapper;
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
 * Integration tests for the {@link PilotoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PilotoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "27769712N";
    private static final String UPDATED_DNI = "96234271T";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "yE4}@AU/k.6D{D*r";
    private static final String UPDATED_EMAIL = "9@1+.BdX6DO";

    private static final Integer DEFAULT_HORAS_VUELO = 0;
    private static final Integer UPDATED_HORAS_VUELO = 1;
    private static final Integer SMALLER_HORAS_VUELO = 0 - 1;

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/pilotos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PilotoRepository pilotoRepository;

    @Autowired
    private PilotoMapper pilotoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPilotoMockMvc;

    private Piloto piloto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piloto createEntity(EntityManager em) {
        Piloto piloto = new Piloto()
            .nombre(DEFAULT_NOMBRE)
            .dni(DEFAULT_DNI)
            .direccion(DEFAULT_DIRECCION)
            .email(DEFAULT_EMAIL)
            .horasVuelo(DEFAULT_HORAS_VUELO)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
        return piloto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piloto createUpdatedEntity(EntityManager em) {
        Piloto piloto = new Piloto()
            .nombre(UPDATED_NOMBRE)
            .dni(UPDATED_DNI)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .horasVuelo(UPDATED_HORAS_VUELO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        return piloto;
    }

    @BeforeEach
    public void initTest() {
        piloto = createEntity(em);
    }

    @Test
    @Transactional
    void createPiloto() throws Exception {
        int databaseSizeBeforeCreate = pilotoRepository.findAll().size();
        // Create the Piloto
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);
        restPilotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilotoDTO)))
            .andExpect(status().isCreated());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeCreate + 1);
        Piloto testPiloto = pilotoList.get(pilotoList.size() - 1);
        assertThat(testPiloto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPiloto.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testPiloto.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testPiloto.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPiloto.getHorasVuelo()).isEqualTo(DEFAULT_HORAS_VUELO);
        assertThat(testPiloto.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testPiloto.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createPilotoWithExistingId() throws Exception {
        // Create the Piloto with an existing ID
        piloto.setId(1L);
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        int databaseSizeBeforeCreate = pilotoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPilotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = pilotoRepository.findAll().size();
        // set the field null
        piloto.setNombre(null);

        // Create the Piloto, which fails.
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        restPilotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilotoDTO)))
            .andExpect(status().isBadRequest());

        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        int databaseSizeBeforeTest = pilotoRepository.findAll().size();
        // set the field null
        piloto.setDni(null);

        // Create the Piloto, which fails.
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        restPilotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilotoDTO)))
            .andExpect(status().isBadRequest());

        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = pilotoRepository.findAll().size();
        // set the field null
        piloto.setEmail(null);

        // Create the Piloto, which fails.
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        restPilotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilotoDTO)))
            .andExpect(status().isBadRequest());

        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHorasVueloIsRequired() throws Exception {
        int databaseSizeBeforeTest = pilotoRepository.findAll().size();
        // set the field null
        piloto.setHorasVuelo(null);

        // Create the Piloto, which fails.
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        restPilotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilotoDTO)))
            .andExpect(status().isBadRequest());

        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPilotos() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList
        restPilotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(piloto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].horasVuelo").value(hasItem(DEFAULT_HORAS_VUELO)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))));
    }

    @Test
    @Transactional
    void getPiloto() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get the piloto
        restPilotoMockMvc
            .perform(get(ENTITY_API_URL_ID, piloto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(piloto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.horasVuelo").value(DEFAULT_HORAS_VUELO))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    void getPilotosByIdFiltering() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        Long id = piloto.getId();

        defaultPilotoShouldBeFound("id.equals=" + id);
        defaultPilotoShouldNotBeFound("id.notEquals=" + id);

        defaultPilotoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPilotoShouldNotBeFound("id.greaterThan=" + id);

        defaultPilotoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPilotoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPilotosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where nombre equals to DEFAULT_NOMBRE
        defaultPilotoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the pilotoList where nombre equals to UPDATED_NOMBRE
        defaultPilotoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPilotosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where nombre not equals to DEFAULT_NOMBRE
        defaultPilotoShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the pilotoList where nombre not equals to UPDATED_NOMBRE
        defaultPilotoShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPilotosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPilotoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the pilotoList where nombre equals to UPDATED_NOMBRE
        defaultPilotoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPilotosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where nombre is not null
        defaultPilotoShouldBeFound("nombre.specified=true");

        // Get all the pilotoList where nombre is null
        defaultPilotoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPilotosByNombreContainsSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where nombre contains DEFAULT_NOMBRE
        defaultPilotoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the pilotoList where nombre contains UPDATED_NOMBRE
        defaultPilotoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPilotosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where nombre does not contain DEFAULT_NOMBRE
        defaultPilotoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the pilotoList where nombre does not contain UPDATED_NOMBRE
        defaultPilotoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPilotosByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where dni equals to DEFAULT_DNI
        defaultPilotoShouldBeFound("dni.equals=" + DEFAULT_DNI);

        // Get all the pilotoList where dni equals to UPDATED_DNI
        defaultPilotoShouldNotBeFound("dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllPilotosByDniIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where dni not equals to DEFAULT_DNI
        defaultPilotoShouldNotBeFound("dni.notEquals=" + DEFAULT_DNI);

        // Get all the pilotoList where dni not equals to UPDATED_DNI
        defaultPilotoShouldBeFound("dni.notEquals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllPilotosByDniIsInShouldWork() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where dni in DEFAULT_DNI or UPDATED_DNI
        defaultPilotoShouldBeFound("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI);

        // Get all the pilotoList where dni equals to UPDATED_DNI
        defaultPilotoShouldNotBeFound("dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllPilotosByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where dni is not null
        defaultPilotoShouldBeFound("dni.specified=true");

        // Get all the pilotoList where dni is null
        defaultPilotoShouldNotBeFound("dni.specified=false");
    }

    @Test
    @Transactional
    void getAllPilotosByDniContainsSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where dni contains DEFAULT_DNI
        defaultPilotoShouldBeFound("dni.contains=" + DEFAULT_DNI);

        // Get all the pilotoList where dni contains UPDATED_DNI
        defaultPilotoShouldNotBeFound("dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllPilotosByDniNotContainsSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where dni does not contain DEFAULT_DNI
        defaultPilotoShouldNotBeFound("dni.doesNotContain=" + DEFAULT_DNI);

        // Get all the pilotoList where dni does not contain UPDATED_DNI
        defaultPilotoShouldBeFound("dni.doesNotContain=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllPilotosByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where direccion equals to DEFAULT_DIRECCION
        defaultPilotoShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the pilotoList where direccion equals to UPDATED_DIRECCION
        defaultPilotoShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPilotosByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where direccion not equals to DEFAULT_DIRECCION
        defaultPilotoShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the pilotoList where direccion not equals to UPDATED_DIRECCION
        defaultPilotoShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPilotosByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultPilotoShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the pilotoList where direccion equals to UPDATED_DIRECCION
        defaultPilotoShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPilotosByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where direccion is not null
        defaultPilotoShouldBeFound("direccion.specified=true");

        // Get all the pilotoList where direccion is null
        defaultPilotoShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllPilotosByDireccionContainsSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where direccion contains DEFAULT_DIRECCION
        defaultPilotoShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the pilotoList where direccion contains UPDATED_DIRECCION
        defaultPilotoShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPilotosByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where direccion does not contain DEFAULT_DIRECCION
        defaultPilotoShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the pilotoList where direccion does not contain UPDATED_DIRECCION
        defaultPilotoShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPilotosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where email equals to DEFAULT_EMAIL
        defaultPilotoShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the pilotoList where email equals to UPDATED_EMAIL
        defaultPilotoShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPilotosByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where email not equals to DEFAULT_EMAIL
        defaultPilotoShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the pilotoList where email not equals to UPDATED_EMAIL
        defaultPilotoShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPilotosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPilotoShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the pilotoList where email equals to UPDATED_EMAIL
        defaultPilotoShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPilotosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where email is not null
        defaultPilotoShouldBeFound("email.specified=true");

        // Get all the pilotoList where email is null
        defaultPilotoShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPilotosByEmailContainsSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where email contains DEFAULT_EMAIL
        defaultPilotoShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the pilotoList where email contains UPDATED_EMAIL
        defaultPilotoShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPilotosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where email does not contain DEFAULT_EMAIL
        defaultPilotoShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the pilotoList where email does not contain UPDATED_EMAIL
        defaultPilotoShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPilotosByHorasVueloIsEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where horasVuelo equals to DEFAULT_HORAS_VUELO
        defaultPilotoShouldBeFound("horasVuelo.equals=" + DEFAULT_HORAS_VUELO);

        // Get all the pilotoList where horasVuelo equals to UPDATED_HORAS_VUELO
        defaultPilotoShouldNotBeFound("horasVuelo.equals=" + UPDATED_HORAS_VUELO);
    }

    @Test
    @Transactional
    void getAllPilotosByHorasVueloIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where horasVuelo not equals to DEFAULT_HORAS_VUELO
        defaultPilotoShouldNotBeFound("horasVuelo.notEquals=" + DEFAULT_HORAS_VUELO);

        // Get all the pilotoList where horasVuelo not equals to UPDATED_HORAS_VUELO
        defaultPilotoShouldBeFound("horasVuelo.notEquals=" + UPDATED_HORAS_VUELO);
    }

    @Test
    @Transactional
    void getAllPilotosByHorasVueloIsInShouldWork() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where horasVuelo in DEFAULT_HORAS_VUELO or UPDATED_HORAS_VUELO
        defaultPilotoShouldBeFound("horasVuelo.in=" + DEFAULT_HORAS_VUELO + "," + UPDATED_HORAS_VUELO);

        // Get all the pilotoList where horasVuelo equals to UPDATED_HORAS_VUELO
        defaultPilotoShouldNotBeFound("horasVuelo.in=" + UPDATED_HORAS_VUELO);
    }

    @Test
    @Transactional
    void getAllPilotosByHorasVueloIsNullOrNotNull() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where horasVuelo is not null
        defaultPilotoShouldBeFound("horasVuelo.specified=true");

        // Get all the pilotoList where horasVuelo is null
        defaultPilotoShouldNotBeFound("horasVuelo.specified=false");
    }

    @Test
    @Transactional
    void getAllPilotosByHorasVueloIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where horasVuelo is greater than or equal to DEFAULT_HORAS_VUELO
        defaultPilotoShouldBeFound("horasVuelo.greaterThanOrEqual=" + DEFAULT_HORAS_VUELO);

        // Get all the pilotoList where horasVuelo is greater than or equal to UPDATED_HORAS_VUELO
        defaultPilotoShouldNotBeFound("horasVuelo.greaterThanOrEqual=" + UPDATED_HORAS_VUELO);
    }

    @Test
    @Transactional
    void getAllPilotosByHorasVueloIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where horasVuelo is less than or equal to DEFAULT_HORAS_VUELO
        defaultPilotoShouldBeFound("horasVuelo.lessThanOrEqual=" + DEFAULT_HORAS_VUELO);

        // Get all the pilotoList where horasVuelo is less than or equal to SMALLER_HORAS_VUELO
        defaultPilotoShouldNotBeFound("horasVuelo.lessThanOrEqual=" + SMALLER_HORAS_VUELO);
    }

    @Test
    @Transactional
    void getAllPilotosByHorasVueloIsLessThanSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where horasVuelo is less than DEFAULT_HORAS_VUELO
        defaultPilotoShouldNotBeFound("horasVuelo.lessThan=" + DEFAULT_HORAS_VUELO);

        // Get all the pilotoList where horasVuelo is less than UPDATED_HORAS_VUELO
        defaultPilotoShouldBeFound("horasVuelo.lessThan=" + UPDATED_HORAS_VUELO);
    }

    @Test
    @Transactional
    void getAllPilotosByHorasVueloIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList where horasVuelo is greater than DEFAULT_HORAS_VUELO
        defaultPilotoShouldNotBeFound("horasVuelo.greaterThan=" + DEFAULT_HORAS_VUELO);

        // Get all the pilotoList where horasVuelo is greater than SMALLER_HORAS_VUELO
        defaultPilotoShouldBeFound("horasVuelo.greaterThan=" + SMALLER_HORAS_VUELO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPilotoShouldBeFound(String filter) throws Exception {
        restPilotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(piloto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].horasVuelo").value(hasItem(DEFAULT_HORAS_VUELO)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))));

        // Check, that the count call also returns 1
        restPilotoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPilotoShouldNotBeFound(String filter) throws Exception {
        restPilotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPilotoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPiloto() throws Exception {
        // Get the piloto
        restPilotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPiloto() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();

        // Update the piloto
        Piloto updatedPiloto = pilotoRepository.findById(piloto.getId()).get();
        // Disconnect from session so that the updates on updatedPiloto are not directly saved in db
        em.detach(updatedPiloto);
        updatedPiloto
            .nombre(UPDATED_NOMBRE)
            .dni(UPDATED_DNI)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .horasVuelo(UPDATED_HORAS_VUELO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        PilotoDTO pilotoDTO = pilotoMapper.toDto(updatedPiloto);

        restPilotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pilotoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pilotoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
        Piloto testPiloto = pilotoList.get(pilotoList.size() - 1);
        assertThat(testPiloto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPiloto.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testPiloto.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testPiloto.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPiloto.getHorasVuelo()).isEqualTo(UPDATED_HORAS_VUELO);
        assertThat(testPiloto.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testPiloto.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // Create the Piloto
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pilotoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pilotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // Create the Piloto
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pilotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // Create the Piloto
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pilotoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePilotoWithPatch() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();

        // Update the piloto using partial update
        Piloto partialUpdatedPiloto = new Piloto();
        partialUpdatedPiloto.setId(piloto.getId());

        partialUpdatedPiloto
            .email(UPDATED_EMAIL)
            .horasVuelo(UPDATED_HORAS_VUELO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restPilotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPiloto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPiloto))
            )
            .andExpect(status().isOk());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
        Piloto testPiloto = pilotoList.get(pilotoList.size() - 1);
        assertThat(testPiloto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPiloto.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testPiloto.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testPiloto.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPiloto.getHorasVuelo()).isEqualTo(UPDATED_HORAS_VUELO);
        assertThat(testPiloto.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testPiloto.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePilotoWithPatch() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();

        // Update the piloto using partial update
        Piloto partialUpdatedPiloto = new Piloto();
        partialUpdatedPiloto.setId(piloto.getId());

        partialUpdatedPiloto
            .nombre(UPDATED_NOMBRE)
            .dni(UPDATED_DNI)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .horasVuelo(UPDATED_HORAS_VUELO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restPilotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPiloto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPiloto))
            )
            .andExpect(status().isOk());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
        Piloto testPiloto = pilotoList.get(pilotoList.size() - 1);
        assertThat(testPiloto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPiloto.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testPiloto.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testPiloto.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPiloto.getHorasVuelo()).isEqualTo(UPDATED_HORAS_VUELO);
        assertThat(testPiloto.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testPiloto.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // Create the Piloto
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pilotoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pilotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // Create the Piloto
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pilotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // Create the Piloto
        PilotoDTO pilotoDTO = pilotoMapper.toDto(piloto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pilotoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePiloto() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        int databaseSizeBeforeDelete = pilotoRepository.findAll().size();

        // Delete the piloto
        restPilotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, piloto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
