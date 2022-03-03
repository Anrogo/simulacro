import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVuelo, Vuelo } from '../vuelo.model';
import { VueloService } from '../service/vuelo.service';
import { IAeropuerto } from 'app/entities/aeropuerto/aeropuerto.model';
import { AeropuertoService } from 'app/entities/aeropuerto/service/aeropuerto.service';
import { IAvion } from 'app/entities/avion/avion.model';
import { AvionService } from 'app/entities/avion/service/avion.service';
import { IPiloto } from 'app/entities/piloto/piloto.model';
import { PilotoService } from 'app/entities/piloto/service/piloto.service';
import { ITripulacion } from 'app/entities/tripulacion/tripulacion.model';
import { TripulacionService } from 'app/entities/tripulacion/service/tripulacion.service';

@Component({
  selector: 'jhi-vuelo-update',
  templateUrl: './vuelo-update.component.html',
})
export class VueloUpdateComponent implements OnInit {
  isSaving = false;

  aeropuertosSharedCollection: IAeropuerto[] = [];
  avionsSharedCollection: IAvion[] = [];
  pilotosSharedCollection: IPiloto[] = [];
  tripulacionsSharedCollection: ITripulacion[] = [];

  editForm = this.fb.group({
    id: [],
    numVuelo: [null, [Validators.required, Validators.pattern('^[A-Z]{2}(?:-[0-9]{4})*$')]],
    hora: [null, [Validators.required]],
    origen: [],
    destino: [],
    avion: [],
    piloto: [],
    tripulantes: [],
  });

  constructor(
    protected vueloService: VueloService,
    protected aeropuertoService: AeropuertoService,
    protected avionService: AvionService,
    protected pilotoService: PilotoService,
    protected tripulacionService: TripulacionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vuelo }) => {
      if (vuelo.id === undefined) {
        const today = dayjs().startOf('day');
        vuelo.hora = today;
      }

      this.updateForm(vuelo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vuelo = this.createFromForm();
    if (vuelo.id !== undefined) {
      this.subscribeToSaveResponse(this.vueloService.update(vuelo));
    } else {
      this.subscribeToSaveResponse(this.vueloService.create(vuelo));
    }
  }

  trackAeropuertoById(index: number, item: IAeropuerto): number {
    return item.id!;
  }

  trackAvionById(index: number, item: IAvion): number {
    return item.id!;
  }

  trackPilotoById(index: number, item: IPiloto): number {
    return item.id!;
  }

  trackTripulacionById(index: number, item: ITripulacion): number {
    return item.id!;
  }

  getSelectedTripulacion(option: ITripulacion, selectedVals?: ITripulacion[]): ITripulacion {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVuelo>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vuelo: IVuelo): void {
    this.editForm.patchValue({
      id: vuelo.id,
      numVuelo: vuelo.numVuelo,
      hora: vuelo.hora ? vuelo.hora.format(DATE_TIME_FORMAT) : null,
      origen: vuelo.origen,
      destino: vuelo.destino,
      avion: vuelo.avion,
      piloto: vuelo.piloto,
      tripulantes: vuelo.tripulantes,
    });

    this.aeropuertosSharedCollection = this.aeropuertoService.addAeropuertoToCollectionIfMissing(
      this.aeropuertosSharedCollection,
      vuelo.origen,
      vuelo.destino
    );
    this.avionsSharedCollection = this.avionService.addAvionToCollectionIfMissing(this.avionsSharedCollection, vuelo.avion);
    this.pilotosSharedCollection = this.pilotoService.addPilotoToCollectionIfMissing(this.pilotosSharedCollection, vuelo.piloto);
    this.tripulacionsSharedCollection = this.tripulacionService.addTripulacionToCollectionIfMissing(
      this.tripulacionsSharedCollection,
      ...(vuelo.tripulantes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.aeropuertoService
      .query()
      .pipe(map((res: HttpResponse<IAeropuerto[]>) => res.body ?? []))
      .pipe(
        map((aeropuertos: IAeropuerto[]) =>
          this.aeropuertoService.addAeropuertoToCollectionIfMissing(
            aeropuertos,
            this.editForm.get('origen')!.value,
            this.editForm.get('destino')!.value
          )
        )
      )
      .subscribe((aeropuertos: IAeropuerto[]) => (this.aeropuertosSharedCollection = aeropuertos));

    this.avionService
      .query()
      .pipe(map((res: HttpResponse<IAvion[]>) => res.body ?? []))
      .pipe(map((avions: IAvion[]) => this.avionService.addAvionToCollectionIfMissing(avions, this.editForm.get('avion')!.value)))
      .subscribe((avions: IAvion[]) => (this.avionsSharedCollection = avions));

    this.pilotoService
      .query()
      .pipe(map((res: HttpResponse<IPiloto[]>) => res.body ?? []))
      .pipe(map((pilotos: IPiloto[]) => this.pilotoService.addPilotoToCollectionIfMissing(pilotos, this.editForm.get('piloto')!.value)))
      .subscribe((pilotos: IPiloto[]) => (this.pilotosSharedCollection = pilotos));

    this.tripulacionService
      .query()
      .pipe(map((res: HttpResponse<ITripulacion[]>) => res.body ?? []))
      .pipe(
        map((tripulacions: ITripulacion[]) =>
          this.tripulacionService.addTripulacionToCollectionIfMissing(tripulacions, ...(this.editForm.get('tripulantes')!.value ?? []))
        )
      )
      .subscribe((tripulacions: ITripulacion[]) => (this.tripulacionsSharedCollection = tripulacions));
  }

  protected createFromForm(): IVuelo {
    return {
      ...new Vuelo(),
      id: this.editForm.get(['id'])!.value,
      numVuelo: this.editForm.get(['numVuelo'])!.value,
      hora: this.editForm.get(['hora'])!.value ? dayjs(this.editForm.get(['hora'])!.value, DATE_TIME_FORMAT) : undefined,
      origen: this.editForm.get(['origen'])!.value,
      destino: this.editForm.get(['destino'])!.value,
      avion: this.editForm.get(['avion'])!.value,
      piloto: this.editForm.get(['piloto'])!.value,
      tripulantes: this.editForm.get(['tripulantes'])!.value,
    };
  }
}
