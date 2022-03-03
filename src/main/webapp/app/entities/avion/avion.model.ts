import { IVuelo } from 'app/entities/vuelo/vuelo.model';

export interface IAvion {
  id?: number;
  tipo?: string;
  edad?: number;
  numSerie?: string;
  matricula?: string;
  vuelos?: IVuelo[] | null;
}

export class Avion implements IAvion {
  constructor(
    public id?: number,
    public tipo?: string,
    public edad?: number,
    public numSerie?: string,
    public matricula?: string,
    public vuelos?: IVuelo[] | null
  ) {}
}

export function getAvionIdentifier(avion: IAvion): number | undefined {
  return avion.id;
}
