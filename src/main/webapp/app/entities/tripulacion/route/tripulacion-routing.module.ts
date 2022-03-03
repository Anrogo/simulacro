import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TripulacionComponent } from '../list/tripulacion.component';
import { TripulacionDetailComponent } from '../detail/tripulacion-detail.component';
import { TripulacionUpdateComponent } from '../update/tripulacion-update.component';
import { TripulacionRoutingResolveService } from './tripulacion-routing-resolve.service';

const tripulacionRoute: Routes = [
  {
    path: '',
    component: TripulacionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TripulacionDetailComponent,
    resolve: {
      tripulacion: TripulacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TripulacionUpdateComponent,
    resolve: {
      tripulacion: TripulacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TripulacionUpdateComponent,
    resolve: {
      tripulacion: TripulacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tripulacionRoute)],
  exports: [RouterModule],
})
export class TripulacionRoutingModule {}
