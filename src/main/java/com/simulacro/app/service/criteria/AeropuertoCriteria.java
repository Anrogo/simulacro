package com.simulacro.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.simulacro.app.domain.Aeropuerto} entity. This class is used
 * in {@link com.simulacro.app.web.rest.AeropuertoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /aeropuertos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AeropuertoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter ciudad;

    private LongFilter salidasId;

    private LongFilter llegadasId;

    private Boolean distinct;

    public AeropuertoCriteria() {}

    public AeropuertoCriteria(AeropuertoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.ciudad = other.ciudad == null ? null : other.ciudad.copy();
        this.salidasId = other.salidasId == null ? null : other.salidasId.copy();
        this.llegadasId = other.llegadasId == null ? null : other.llegadasId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AeropuertoCriteria copy() {
        return new AeropuertoCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getCiudad() {
        return ciudad;
    }

    public StringFilter ciudad() {
        if (ciudad == null) {
            ciudad = new StringFilter();
        }
        return ciudad;
    }

    public void setCiudad(StringFilter ciudad) {
        this.ciudad = ciudad;
    }

    public LongFilter getSalidasId() {
        return salidasId;
    }

    public LongFilter salidasId() {
        if (salidasId == null) {
            salidasId = new LongFilter();
        }
        return salidasId;
    }

    public void setSalidasId(LongFilter salidasId) {
        this.salidasId = salidasId;
    }

    public LongFilter getLlegadasId() {
        return llegadasId;
    }

    public LongFilter llegadasId() {
        if (llegadasId == null) {
            llegadasId = new LongFilter();
        }
        return llegadasId;
    }

    public void setLlegadasId(LongFilter llegadasId) {
        this.llegadasId = llegadasId;
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
        final AeropuertoCriteria that = (AeropuertoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(ciudad, that.ciudad) &&
            Objects.equals(salidasId, that.salidasId) &&
            Objects.equals(llegadasId, that.llegadasId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, ciudad, salidasId, llegadasId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AeropuertoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (ciudad != null ? "ciudad=" + ciudad + ", " : "") +
            (salidasId != null ? "salidasId=" + salidasId + ", " : "") +
            (llegadasId != null ? "llegadasId=" + llegadasId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
