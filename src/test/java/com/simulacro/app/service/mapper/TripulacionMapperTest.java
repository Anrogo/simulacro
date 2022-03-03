package com.simulacro.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripulacionMapperTest {

    private TripulacionMapper tripulacionMapper;

    @BeforeEach
    public void setUp() {
        tripulacionMapper = new TripulacionMapperImpl();
    }
}
