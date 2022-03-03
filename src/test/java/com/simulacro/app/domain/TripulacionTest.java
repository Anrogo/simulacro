package com.simulacro.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.simulacro.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripulacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tripulacion.class);
        Tripulacion tripulacion1 = new Tripulacion();
        tripulacion1.setId(1L);
        Tripulacion tripulacion2 = new Tripulacion();
        tripulacion2.setId(tripulacion1.getId());
        assertThat(tripulacion1).isEqualTo(tripulacion2);
        tripulacion2.setId(2L);
        assertThat(tripulacion1).isNotEqualTo(tripulacion2);
        tripulacion1.setId(null);
        assertThat(tripulacion1).isNotEqualTo(tripulacion2);
    }
}
