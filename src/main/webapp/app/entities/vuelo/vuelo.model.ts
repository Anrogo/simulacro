import dayjs from 'dayjs/esm';
import { IAeropuerto } from 'app/entities/aeropuerto/aeropuerto.model';

export interface IVuelo {
  id?: number;
  numVuelo?: string;
  hora?: dayjs.Dayjs;
  origen?: IAeropuerto | null;
  destino?: IAeropuerto | null;
}

export class Vuelo implements IVuelo {
  constructor(
    public id?: number,
    public numVuelo?: string,
    public hora?: dayjs.Dayjs,
    public origen?: IAeropuerto | null,
    public destino?: IAeropuerto | null
  ) {}
}

export function getVueloIdentifier(vuelo: IVuelo): number | undefined {
  return vuelo.id;
}
