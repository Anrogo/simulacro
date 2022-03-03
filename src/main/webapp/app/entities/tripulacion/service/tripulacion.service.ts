import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITripulacion, getTripulacionIdentifier } from '../tripulacion.model';

export type EntityResponseType = HttpResponse<ITripulacion>;
export type EntityArrayResponseType = HttpResponse<ITripulacion[]>;

@Injectable({ providedIn: 'root' })
export class TripulacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tripulacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tripulacion: ITripulacion): Observable<EntityResponseType> {
    return this.http.post<ITripulacion>(this.resourceUrl, tripulacion, { observe: 'response' });
  }

  update(tripulacion: ITripulacion): Observable<EntityResponseType> {
    return this.http.put<ITripulacion>(`${this.resourceUrl}/${getTripulacionIdentifier(tripulacion) as number}`, tripulacion, {
      observe: 'response',
    });
  }

  partialUpdate(tripulacion: ITripulacion): Observable<EntityResponseType> {
    return this.http.patch<ITripulacion>(`${this.resourceUrl}/${getTripulacionIdentifier(tripulacion) as number}`, tripulacion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITripulacion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITripulacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTripulacionToCollectionIfMissing(
    tripulacionCollection: ITripulacion[],
    ...tripulacionsToCheck: (ITripulacion | null | undefined)[]
  ): ITripulacion[] {
    const tripulacions: ITripulacion[] = tripulacionsToCheck.filter(isPresent);
    if (tripulacions.length > 0) {
      const tripulacionCollectionIdentifiers = tripulacionCollection.map(tripulacionItem => getTripulacionIdentifier(tripulacionItem)!);
      const tripulacionsToAdd = tripulacions.filter(tripulacionItem => {
        const tripulacionIdentifier = getTripulacionIdentifier(tripulacionItem);
        if (tripulacionIdentifier == null || tripulacionCollectionIdentifiers.includes(tripulacionIdentifier)) {
          return false;
        }
        tripulacionCollectionIdentifiers.push(tripulacionIdentifier);
        return true;
      });
      return [...tripulacionsToAdd, ...tripulacionCollection];
    }
    return tripulacionCollection;
  }
}
