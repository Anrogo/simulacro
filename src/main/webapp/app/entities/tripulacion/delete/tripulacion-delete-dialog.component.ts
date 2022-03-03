import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITripulacion } from '../tripulacion.model';
import { TripulacionService } from '../service/tripulacion.service';

@Component({
  templateUrl: './tripulacion-delete-dialog.component.html',
})
export class TripulacionDeleteDialogComponent {
  tripulacion?: ITripulacion;

  constructor(protected tripulacionService: TripulacionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tripulacionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
