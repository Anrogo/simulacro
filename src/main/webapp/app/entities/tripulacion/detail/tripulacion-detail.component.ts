import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITripulacion } from '../tripulacion.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tripulacion-detail',
  templateUrl: './tripulacion-detail.component.html',
})
export class TripulacionDetailComponent implements OnInit {
  tripulacion: ITripulacion | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tripulacion }) => {
      this.tripulacion = tripulacion;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
