import dayjs from 'dayjs/esm';
import { IAeropuerto } from 'app/entities/aeropuerto/aeropuerto.model';
import { IAvion } from 'app/entities/avion/avion.model';
import { IPiloto } from 'app/entities/piloto/piloto.model';
import { ITripulacion } from 'app/entities/tripulacion/tripulacion.model';

export interface IVuelo {
  id?: number;
  numVuelo?: string;
  hora?: dayjs.Dayjs;
  origen?: IAeropuerto | null;
  destino?: IAeropuerto | null;
  avion?: IAvion | null;
  piloto?: IPiloto | null;
  tripulantes?: ITripulacion[] | null;
}

export class Vuelo implements IVuelo {
  constructor(
    public id?: number,
    public numVuelo?: string,
    public hora?: dayjs.Dayjs,
    public origen?: IAeropuerto | null,
    public destino?: IAeropuerto | null,
    public avion?: IAvion | null,
    public piloto?: IPiloto | null,
    public tripulantes?: ITripulacion[] | null
  ) {}
}

export function getVueloIdentifier(vuelo: IVuelo): number | undefined {
  return vuelo.id;
}
