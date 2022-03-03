export interface IAvion {
  id?: number;
  tipo?: string;
  edad?: number;
  numSerie?: string;
  matricula?: string;
}

export class Avion implements IAvion {
  constructor(public id?: number, public tipo?: string, public edad?: number, public numSerie?: string, public matricula?: string) {}
}

export function getAvionIdentifier(avion: IAvion): number | undefined {
  return avion.id;
}
