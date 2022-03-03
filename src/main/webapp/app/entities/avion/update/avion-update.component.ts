import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAvion, Avion } from '../avion.model';
import { AvionService } from '../service/avion.service';

@Component({
  selector: 'jhi-avion-update',
  templateUrl: './avion-update.component.html',
})
export class AvionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tipo: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
    edad: [null, [Validators.required]],
    numSerie: [null, [Validators.required, Validators.pattern('^[a-zA-Z0-9]{13}$')]],
    matricula: [null, [Validators.required, Validators.pattern('^[a-zA-Z0-9]{2}(?:-[a-zA-Z0-9]{3})*$')]],
  });

  constructor(protected avionService: AvionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avion }) => {
      this.updateForm(avion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const avion = this.createFromForm();
    if (avion.id !== undefined) {
      this.subscribeToSaveResponse(this.avionService.update(avion));
    } else {
      this.subscribeToSaveResponse(this.avionService.create(avion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAvion>>): void {
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

  protected updateForm(avion: IAvion): void {
    this.editForm.patchValue({
      id: avion.id,
      tipo: avion.tipo,
      edad: avion.edad,
      numSerie: avion.numSerie,
      matricula: avion.matricula,
    });
  }

  protected createFromForm(): IAvion {
    return {
      ...new Avion(),
      id: this.editForm.get(['id'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      edad: this.editForm.get(['edad'])!.value,
      numSerie: this.editForm.get(['numSerie'])!.value,
      matricula: this.editForm.get(['matricula'])!.value,
    };
  }
}
