package com.simulacro.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simulacro.app.domain.Vuelo} entity.
 */
public class VueloDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[A-Z]{2}(?:-[0-9]{4})*$")
    private String numVuelo;

    @NotNull
    private Instant hora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumVuelo() {
        return numVuelo;
    }

    public void setNumVuelo(String numVuelo) {
        this.numVuelo = numVuelo;
    }

    public Instant getHora() {
        return hora;
    }

    public void setHora(Instant hora) {
        this.hora = hora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VueloDTO)) {
            return false;
        }

        VueloDTO vueloDTO = (VueloDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vueloDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VueloDTO{" +
            "id=" + getId() +
            ", numVuelo='" + getNumVuelo() + "'" +
            ", hora='" + getHora() + "'" +
            "}";
    }
}
