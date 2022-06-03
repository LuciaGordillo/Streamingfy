import dayjs from 'dayjs/esm';
import { IPerfiles } from 'app/entities/perfiles/perfiles.model';
import { ILista } from 'app/entities/lista/lista.model';

export interface IPelicula {
  id?: number;
  titulo?: string | null;
  genero?: string | null;
  descripcion?: string | null;
  calificacion?: number | null;
  estreno?: dayjs.Dayjs | null;
  imagen?: string | null;
  url?: string | null;
  perfiles?: IPerfiles[] | null;
  listas?: ILista[] | null;
}

export class Pelicula implements IPelicula {
  constructor(
    public id?: number,
    public titulo?: string | null,
    public genero?: string | null,
    public descripcion?: string | null,
    public calificacion?: number | null,
    public estreno?: dayjs.Dayjs | null,
    public imagen?: string | null,
    public url?: string | null,
    public perfiles?: IPerfiles[] | null,
    public listas?: ILista[] | null
  ) {}
}

export function getPeliculaIdentifier(pelicula: IPelicula): number | undefined {
  return pelicula.id;
}
