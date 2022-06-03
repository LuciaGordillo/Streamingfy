import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { IPerfiles } from 'app/entities/perfiles/perfiles.model';

export interface ILista {
  id?: number;
  countryName?: string | null;
  pelicula?: IPelicula | null;
  perfiles?: IPerfiles | null;
}

export class Lista implements ILista {
  constructor(
    public id?: number,
    public countryName?: string | null,
    public pelicula?: IPelicula | null,
    public perfiles?: IPerfiles | null
  ) {}
}

export function getListaIdentifier(lista: ILista): number | undefined {
  return lista.id;
}
