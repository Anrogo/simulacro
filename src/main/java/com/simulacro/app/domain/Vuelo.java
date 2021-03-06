package com.simulacro.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vuelo.
 */
@Entity
@Table(name = "vuelo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vuelo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^[A-Z]{2}(?:-[0-9]{4})*$")
    @Column(name = "num_vuelo", nullable = false)
    private String numVuelo;

    @NotNull
    @Column(name = "hora", nullable = false)
    private Instant hora;

    @ManyToOne
    @JsonIgnoreProperties(value = { "salidas", "llegadas" }, allowSetters = true)
    private Aeropuerto origen;

    @ManyToOne
    @JsonIgnoreProperties(value = { "salidas", "llegadas" }, allowSetters = true)
    private Aeropuerto destino;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vuelos" }, allowSetters = true)
    private Avion avion;

    @ManyToOne
    private Piloto piloto;

    @ManyToMany
    @JoinTable(
        name = "rel_vuelo__tripulante",
        joinColumns = @JoinColumn(name = "vuelo_id"),
        inverseJoinColumns = @JoinColumn(name = "tripulante_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Tripulacion> tripulantes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vuelo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumVuelo() {
        return this.numVuelo;
    }

    public Vuelo numVuelo(String numVuelo) {
        this.setNumVuelo(numVuelo);
        return this;
    }

    public void setNumVuelo(String numVuelo) {
        this.numVuelo = numVuelo;
    }

    public Instant getHora() {
        return this.hora;
    }

    public Vuelo hora(Instant hora) {
        this.setHora(hora);
        return this;
    }

    public void setHora(Instant hora) {
        this.hora = hora;
    }

    public Aeropuerto getOrigen() {
        return this.origen;
    }

    public void setOrigen(Aeropuerto aeropuerto) {
        this.origen = aeropuerto;
    }

    public Vuelo origen(Aeropuerto aeropuerto) {
        this.setOrigen(aeropuerto);
        return this;
    }

    public Aeropuerto getDestino() {
        return this.destino;
    }

    public void setDestino(Aeropuerto aeropuerto) {
        this.destino = aeropuerto;
    }

    public Vuelo destino(Aeropuerto aeropuerto) {
        this.setDestino(aeropuerto);
        return this;
    }

    public Avion getAvion() {
        return this.avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Vuelo avion(Avion avion) {
        this.setAvion(avion);
        return this;
    }

    public Piloto getPiloto() {
        return this.piloto;
    }

    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    public Vuelo piloto(Piloto piloto) {
        this.setPiloto(piloto);
        return this;
    }

    public Set<Tripulacion> getTripulantes() {
        return this.tripulantes;
    }

    public void setTripulantes(Set<Tripulacion> tripulacions) {
        this.tripulantes = tripulacions;
    }

    public Vuelo tripulantes(Set<Tripulacion> tripulacions) {
        this.setTripulantes(tripulacions);
        return this;
    }

    public Vuelo addTripulante(Tripulacion tripulacion) {
        this.tripulantes.add(tripulacion);
        return this;
    }

    public Vuelo removeTripulante(Tripulacion tripulacion) {
        this.tripulantes.remove(tripulacion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vuelo)) {
            return false;
        }
        return id != null && id.equals(((Vuelo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vuelo{" +
            "id=" + getId() +
            ", numVuelo='" + getNumVuelo() + "'" +
            ", hora='" + getHora() + "'" +
            "}";
    }
}
