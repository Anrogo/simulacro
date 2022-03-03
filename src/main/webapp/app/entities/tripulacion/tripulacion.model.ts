export interface ITripulacion {
  id?: number;
  nombre?: string;
  dni?: string;
  direccion?: string | null;
  email?: string;
  fotoContentType?: string | null;
  foto?: string | null;
}

export class Tripulacion implements ITripulacion {
  constructor(
    public id?: number,
    public nombre?: string,
    public dni?: string,
    public direccion?: string | null,
    public email?: string,
    public fotoContentType?: string | null,
    public foto?: string | null
  ) {}
}

export function getTripulacionIdentifier(tripulacion: ITripulacion): number | undefined {
  return tripulacion.id;
}
