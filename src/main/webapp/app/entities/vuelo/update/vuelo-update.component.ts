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

@Component({
  selector: 'jhi-vuelo-update',
  templateUrl: './vuelo-update.component.html',
})
export class VueloUpdateComponent implements OnInit {
  isSaving = false;

  aeropuertosSharedCollection: IAeropuerto[] = [];

  editForm = this.fb.group({
    id: [],
    numVuelo: [null, [Validators.required, Validators.pattern('^[A-Z]{2}(?:-[0-9]{4})*$')]],
    hora: [null, [Validators.required]],
    origen: [],
    destino: [],
  });

  constructor(
    protected vueloService: VueloService,
    protected aeropuertoService: AeropuertoService,
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
    });

    this.aeropuertosSharedCollection = this.aeropuertoService.addAeropuertoToCollectionIfMissing(
      this.aeropuertosSharedCollection,
      vuelo.origen,
      vuelo.destino
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
  }

  protected createFromForm(): IVuelo {
    return {
      ...new Vuelo(),
      id: this.editForm.get(['id'])!.value,
      numVuelo: this.editForm.get(['numVuelo'])!.value,
      hora: this.editForm.get(['hora'])!.value ? dayjs(this.editForm.get(['hora'])!.value, DATE_TIME_FORMAT) : undefined,
      origen: this.editForm.get(['origen'])!.value,
      destino: this.editForm.get(['destino'])!.value,
    };
  }
}
