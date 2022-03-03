export interface IPiloto {
  id?: number;
  nombre?: string;
  dni?: string;
  direccion?: string | null;
  email?: string;
  horasVuelo?: number;
  fotoContentType?: string | null;
  foto?: string | null;
}

export class Piloto implements IPiloto {
  constructor(
    public id?: number,
    public nombre?: string,
    public dni?: string,
    public direccion?: string | null,
    public email?: string,
    public horasVuelo?: number,
    public fotoContentType?: string | null,
    public foto?: string | null
  ) {}
}

export function getPilotoIdentifier(piloto: IPiloto): number | undefined {
  return piloto.id;
}
