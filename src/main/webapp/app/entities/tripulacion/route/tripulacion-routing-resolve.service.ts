import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITripulacion, Tripulacion } from '../tripulacion.model';
import { TripulacionService } from '../service/tripulacion.service';

@Injectable({ providedIn: 'root' })
export class TripulacionRoutingResolveService implements Resolve<ITripulacion> {
  constructor(protected service: TripulacionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITripulacion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tripulacion: HttpResponse<Tripulacion>) => {
          if (tripulacion.body) {
            return of(tripulacion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tripulacion());
  }
}
