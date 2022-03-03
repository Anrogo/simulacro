package com.simulacro.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

    private AeropuertoDTO origen;

    private AeropuertoDTO destino;

    private AvionDTO avion;

    private PilotoDTO piloto;

    private Set<TripulacionDTO> tripulantes = new HashSet<>();

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

    public AeropuertoDTO getOrigen() {
        return origen;
    }

    public void setOrigen(AeropuertoDTO origen) {
        this.origen = origen;
    }

    public AeropuertoDTO getDestino() {
        return destino;
    }

    public void setDestino(AeropuertoDTO destino) {
        this.destino = destino;
    }

    public AvionDTO getAvion() {
        return avion;
    }

    public void setAvion(AvionDTO avion) {
        this.avion = avion;
    }

    public PilotoDTO getPiloto() {
        return piloto;
    }

    public void setPiloto(PilotoDTO piloto) {
        this.piloto = piloto;
    }

    public Set<TripulacionDTO> getTripulantes() {
        return tripulantes;
    }

    public void setTripulantes(Set<TripulacionDTO> tripulantes) {
        this.tripulantes = tripulantes;
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
            ", origen=" + getOrigen() +
            ", destino=" + getDestino() +
            ", avion=" + getAvion() +
            ", piloto=" + getPiloto() +
            ", tripulantes=" + getTripulantes() +
            "}";
    }
}
