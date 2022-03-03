package com.simulacro.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.simulacro.app.domain.Vuelo} entity. This class is used
 * in {@link com.simulacro.app.web.rest.VueloResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vuelos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class VueloCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numVuelo;

    private InstantFilter hora;

    private LongFilter origenId;

    private LongFilter destinoId;

    private Boolean distinct;

    public VueloCriteria() {}

    public VueloCriteria(VueloCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numVuelo = other.numVuelo == null ? null : other.numVuelo.copy();
        this.hora = other.hora == null ? null : other.hora.copy();
        this.origenId = other.origenId == null ? null : other.origenId.copy();
        this.destinoId = other.destinoId == null ? null : other.destinoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VueloCriteria copy() {
        return new VueloCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumVuelo() {
        return numVuelo;
    }

    public StringFilter numVuelo() {
        if (numVuelo == null) {
            numVuelo = new StringFilter();
        }
        return numVuelo;
    }

    public void setNumVuelo(StringFilter numVuelo) {
        this.numVuelo = numVuelo;
    }

    public InstantFilter getHora() {
        return hora;
    }

    public InstantFilter hora() {
        if (hora == null) {
            hora = new InstantFilter();
        }
        return hora;
    }

    public void setHora(InstantFilter hora) {
        this.hora = hora;
    }

    public LongFilter getOrigenId() {
        return origenId;
    }

    public LongFilter origenId() {
        if (origenId == null) {
            origenId = new LongFilter();
        }
        return origenId;
    }

    public void setOrigenId(LongFilter origenId) {
        this.origenId = origenId;
    }

    public LongFilter getDestinoId() {
        return destinoId;
    }

    public LongFilter destinoId() {
        if (destinoId == null) {
            destinoId = new LongFilter();
        }
        return destinoId;
    }

    public void setDestinoId(LongFilter destinoId) {
        this.destinoId = destinoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VueloCriteria that = (VueloCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numVuelo, that.numVuelo) &&
            Objects.equals(hora, that.hora) &&
            Objects.equals(origenId, that.origenId) &&
            Objects.equals(destinoId, that.destinoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numVuelo, hora, origenId, destinoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VueloCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (numVuelo != null ? "numVuelo=" + numVuelo + ", " : "") +
            (hora != null ? "hora=" + hora + ", " : "") +
            (origenId != null ? "origenId=" + origenId + ", " : "") +
            (destinoId != null ? "destinoId=" + destinoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
