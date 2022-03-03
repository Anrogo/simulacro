package com.simulacro.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simulacro.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripulacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripulacionDTO.class);
        TripulacionDTO tripulacionDTO1 = new TripulacionDTO();
        tripulacionDTO1.setId(1L);
        TripulacionDTO tripulacionDTO2 = new TripulacionDTO();
        assertThat(tripulacionDTO1).isNotEqualTo(tripulacionDTO2);
        tripulacionDTO2.setId(tripulacionDTO1.getId());
        assertThat(tripulacionDTO1).isEqualTo(tripulacionDTO2);
        tripulacionDTO2.setId(2L);
        assertThat(tripulacionDTO1).isNotEqualTo(tripulacionDTO2);
        tripulacionDTO1.setId(null);
        assertThat(tripulacionDTO1).isNotEqualTo(tripulacionDTO2);
    }
}
