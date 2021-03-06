import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'piloto',
        data: { pageTitle: 'Pilotos' },
        loadChildren: () => import('./piloto/piloto.module').then(m => m.PilotoModule),
      },
      {
        path: 'tripulacion',
        data: { pageTitle: 'Tripulacions' },
        loadChildren: () => import('./tripulacion/tripulacion.module').then(m => m.TripulacionModule),
      },
      {
        path: 'avion',
        data: { pageTitle: 'Avions' },
        loadChildren: () => import('./avion/avion.module').then(m => m.AvionModule),
      },
      {
        path: 'vuelo',
        data: { pageTitle: 'Vuelos' },
        loadChildren: () => import('./vuelo/vuelo.module').then(m => m.VueloModule),
      },
      {
        path: 'aeropuerto',
        data: { pageTitle: 'Aeropuertos' },
        loadChildren: () => import('./aeropuerto/aeropuerto.module').then(m => m.AeropuertoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
