import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TripulacionComponent } from './list/tripulacion.component';
import { TripulacionDetailComponent } from './detail/tripulacion-detail.component';
import { TripulacionUpdateComponent } from './update/tripulacion-update.component';
import { TripulacionDeleteDialogComponent } from './delete/tripulacion-delete-dialog.component';
import { TripulacionRoutingModule } from './route/tripulacion-routing.module';

@NgModule({
  imports: [SharedModule, TripulacionRoutingModule],
  declarations: [TripulacionComponent, TripulacionDetailComponent, TripulacionUpdateComponent, TripulacionDeleteDialogComponent],
  entryComponents: [TripulacionDeleteDialogComponent],
})
export class TripulacionModule {}
