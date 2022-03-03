import dayjs from 'dayjs/esm';

export interface IVuelo {
  id?: number;
  numVuelo?: string;
  hora?: dayjs.Dayjs;
}

export class Vuelo implements IVuelo {
  constructor(public id?: number, public numVuelo?: string, public hora?: dayjs.Dayjs) {}
}

export function getVueloIdentifier(vuelo: IVuelo): number | undefined {
  return vuelo.id;
}
