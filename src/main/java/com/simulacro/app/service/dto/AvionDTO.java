package com.simulacro.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simulacro.app.domain.Avion} entity.
 */
public class AvionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    private String tipo;

    @NotNull
    private Integer edad;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{13}$")
    private String numSerie;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{2}(?:-[a-zA-Z0-9]{3})*$")
    private String matricula;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvionDTO)) {
            return false;
        }

        AvionDTO avionDTO = (AvionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, avionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvionDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", edad=" + getEdad() +
            ", numSerie='" + getNumSerie() + "'" +
            ", matricula='" + getMatricula() + "'" +
            "}";
    }
}
